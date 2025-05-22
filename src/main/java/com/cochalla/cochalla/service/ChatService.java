package com.cochalla.cochalla.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.cochalla.cochalla.client.GptAiClient;
import com.cochalla.cochalla.domain.Answer;
import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.Question;
import com.cochalla.cochalla.domain.User;
import com.cochalla.cochalla.dto.ChatListItemDto;
import com.cochalla.cochalla.dto.ChatMessageDto;
import com.cochalla.cochalla.dto.ChatMessageResponseDto;
import com.cochalla.cochalla.dto.GptMessage;
import com.cochalla.cochalla.exception.ForbiddenException;
import com.cochalla.cochalla.exception.GptException;
import com.cochalla.cochalla.exception.NotFoundException;

import com.cochalla.cochalla.repository.ChatRepository;
import com.cochalla.cochalla.repository.QuestionRepository;
import com.cochalla.cochalla.repository.SummaryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

  private final GptAiClient gptClient;

  private final ChatRepository chatRepository;
  private final SummaryRepository summaryRepository;
  private final QuestionRepository questionRepository;

  private final StringRedisTemplate redisTemplate;

  private final UserService userService;

  // == 특정 채팅 대화 조회 ==
  public ChatMessageResponseDto getMessagesInChat(String userId, Integer chatId) {

    User user = userService.findByIdOrThrow(userId);
    getChatOrThrow(chatId, user);

    // 질문/답변 가져오기
    List<Question> questions = questionRepository.findWithAnswersByChatIdAndUserId(chatId, userId);

    List<ChatMessageDto> messages = new ArrayList<>();

    for (Question question : questions) {
      messages.add(ChatMessageDto.builder()
          .id(question.getQuestionId())
          .role("user")
          .content(question.getQuestion())
          .timestamp(question.getCreatedAt())
          .saved(true)
          .build());

      if (question.getAnswer() != null) {
        Answer answer = question.getAnswer();
        messages.add(ChatMessageDto.builder()
            .id(answer.getAnswerId())
            .role("assistant")
            .content(answer.getAnswer())
            .timestamp(answer.getCreatedAt())
            .questionId(question.getQuestionId())
            .saved(true)
            .build());
      }
    }

    boolean isSummarized = summaryRepository.existsByChat_ChatId(chatId);
    boolean isWritable = !isSummarized && !isSummaryBlocked(userId);

    return ChatMessageResponseDto.builder()
        .chatId(chatId)
        .messageList(messages)
        .writable(isWritable)
        .build();
  }

  // == 대화 리스트 조회 (대화정보 없을시 새로운 채팅 생성 )==
  @Transactional
  public List<ChatListItemDto> chatList(String userId) {
    User user = userService.findByIdOrThrow(userId); // 유저 조회
    ChatWithFlag chatResult = getOrCreateWritableChat(user); // 유저가 지금 질문 가능한 최신채팅방 (없으면 새로 생성)
    Chat latestChat = chatResult.chat;

    // 1. 최신 채팅 (질문 가능한 채팅)
    ChatListItemDto latestDto = ChatListItemDto.builder()
        .chatId(latestChat.getChatId())
        .createdAt(latestChat.getCreatedAt())
        .isNew(chatResult.isNewlyCreated)
        .isWritable(chatResult.isWritable)
        .summaryTitle("현재 채팅방")
        .build();

    // 2. 요약된 채팅 리스트
    List<ChatListItemDto> summarizedDtos = summaryRepository.findAllByChat_User_UserId(userId).stream()
        .map(summary -> ChatListItemDto.builder()
            .chatId(summary.getChat().getChatId())
            .createdAt(summary.getChat().getCreatedAt())
            .isNew(false)
            .isWritable(false)
            .summaryTitle(summary.getTitle() != null ? summary.getTitle() : "제목 없음")
            .build())
        .collect(Collectors.toList());

    List<ChatListItemDto> result = new ArrayList<>();
    result.add(latestDto);
    result.addAll(summarizedDtos);
    return result;

  }

  // == 유저 질문 ==
  @Transactional
  public ChatMessageDto askQuestion(String userId, Integer chatId, String questionText) {
    User user = userService.findByIdOrThrow(userId);

    if (questionText == null || questionText.trim().isEmpty() || questionText.length() > 500) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "질문은 1~500자 사이여야 합니다.");
    }

    // 채팅방 존재 여부 확인
    Chat chat = getChatOrThrow(chatId, user);
    if (!chat.getUser().equals(user)) { // 유저의 채팅방인지 확인
      throw new ForbiddenException("사용자 [" + userId + "]는 채팅방 [" + chatId + "]에 접근할 수 없습니다.");
    }

    // Redis에 질문 저장 (5분 TTL)
    String redisKey = "question:" + userId + ":" + chatId;
    redisTemplate.opsForValue().set(redisKey, questionText, Duration.ofMinutes(5));

    // Redis에서 질문 조회
    String questionTextFromRedis = redisTemplate.opsForValue().get(redisKey);

    List<GptMessage> historyMessages;

    long redisStart = System.currentTimeMillis();

    List<String> cachedMessages = redisTemplate.opsForList().range(redisKey + ":history", 0, -1);

    long redisEnd = System.currentTimeMillis();
    log.info("🔴 Redis 조회 시간: {}ms", (redisEnd - redisStart));

    if (questionTextFromRedis == null || questionTextFromRedis.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "저장된 질문이 없습니다. 다시 시도해주세요.");
    }
    if (cachedMessages == null || cachedMessages.isEmpty()) {
      log.info("🔵 Redis 캐시 없음. DB에서 최근 질문/답변 조회");
      List<Question> latestQuestions = questionRepository.findTop3ByChat_ChatIdOrderByCreatedAtDesc(chatId);

      historyMessages = latestQuestions.stream()
          .flatMap(q -> Stream.of(
              new GptMessage("user", q.getQuestion()),
              new GptMessage("assistant", q.getAnswer().getAnswer())))
          .collect(Collectors.toList());

      for (GptMessage msg : historyMessages) {
        redisTemplate.opsForList().rightPush(redisKey + ":history", msg.toJson());
      }
      redisTemplate.expire(redisKey + ":history", Duration.ofMinutes(10));
    } else {
      log.info("🟢 Redis 캐시 사용");
      ObjectMapper objectMapper = new ObjectMapper();
      historyMessages = cachedMessages.stream().map(json -> {
        try {
          return objectMapper.readValue(json, GptMessage.class);
        } catch (JsonProcessingException e) {
          throw new RuntimeException("Redis 캐시 파싱 실패", e);
        }
      }).collect(Collectors.toList());
      // 최신 6개 메시지만 사용
      int max = 6;
      historyMessages = historyMessages.size() <= max
          ? historyMessages
          : historyMessages.subList(historyMessages.size() - max, historyMessages.size());
    }

    // GPT 호출 (최대 3회 재시도)
    String answerText = null;
    Answer answer = null;
    try {
      for (int attempt = 0; attempt < 3; attempt++) {
        try {
          answerText = gptClient.SendMessage(historyMessages, questionTextFromRedis);

          if (isSavable(answerText)) {

            Question question = Question.createQuestion(chat, questionTextFromRedis);
            answer = Answer.createAnswer(question, answerText);

            question.setAnswer(answer);
            questionRepository.save(question);

          }

          break;

        } catch (GptException e) {
          if (e.getStatusCode() == 429) {
            long sleepMs = (long) Math.pow(2, attempt) * 1000; // 1s → 2s → 4s
            log.warn("429 재시도 {}회차: {}ms 대기 후 재시도", attempt + 1, sleepMs);
            try {
              Thread.sleep(sleepMs);
            } catch (InterruptedException ie) {
              log.warn("💤 GPT 재시도 중 인터럽트 발생: {}", questionTextFromRedis);
              break;
            }
          } else {
            throw e;
          }
        }
      }
    } finally {
      redisTemplate.delete(redisKey);
    }

    if (answer != null) {
      return ChatMessageDto.builder()
          .id(answer.getAnswerId())
          .role("assistant")
          .content(answer.getAnswer())
          .timestamp(answer.getCreatedAt())
          .questionId(answer.getQuestion().getQuestionId())
          .saved(true)
          .build();
    } else {
      return ChatMessageDto.builder()
          .id(null)
          .role("assistant")
          .content(answerText)
          .timestamp(LocalDateTime.now())
          .questionId(null)
          .saved(false)
          .build();
    }

  }

  // == 로직 분리 ==
  public record ChatWithFlag(Chat chat, boolean isNewlyCreated, boolean isWritable) {
  }

  private ChatWithFlag getOrCreateWritableChat(User user) {
    // 최근 채팅 조회
    Optional<Chat> recentChatOpt = chatRepository.findTopByUser_UserIdOrderByCreatedAtDesc(user.getUserId());

    // 채팅 기록이 없으면 새 채팅 생성
    if (recentChatOpt.isEmpty()) {
      return new ChatWithFlag(createNewChat(user), true, true);
    }

    Chat recentChat = recentChatOpt.get();

    // 요약 여부 확인
    boolean isSummarized = summaryRepository.existsByChat_ChatId(recentChat.getChatId());

    // 요약되었거나 스케줄러가 작업 중이면 새로 생성
    if (isSummarized || isSummaryBlocked(user.getUserId())) {
      return new ChatWithFlag(createNewChat(user), true, true);
    }

    // 재사용 가능하면 기존 채팅 반환
    return new ChatWithFlag(recentChat, false, true);

  }

  // 새로운 채팅을 생성해서 저장하고 반환
  private Chat createNewChat(User user) {
    Chat newChat = Chat.createChat(user);
    return chatRepository.save(newChat);
  }

  // 스케쥴러가 현재 최신 채팅창의 요약보고서를 스케쥴러에 돌리고 있는지 확인
  public boolean isSummaryBlocked(String userId) {
    String key = "summary:status:" + userId;
    String status = redisTemplate.opsForValue().get(key);
    return status != null;
  }

  private Chat getChatOrThrow(Integer chatId, User user) {
    Chat chat = chatRepository.findById(chatId)
        .orElseThrow(() -> new NotFoundException("채팅방을 찾을 수 없습니다. chatId=" + chatId));

    if (!chat.getUser().equals(user)) {
      throw new ForbiddenException("사용자 [" + user.getUserId() + "]는 채팅방 [" + chatId + "]에 접근할 수 없습니다.");
    }
    return chat;
  }

  private boolean isSavable(String answer) {
    return !(answer.contains("공부 목적에 부합하지 않아") ||
        answer.contains("다시 질문해 주세요") ||
        answer.contains("답변할 수 없습니다") ||
        answer.contains("죄송합니다. 현재 질문의 내용을") ||
        answer.contains("확인할 수 있도록 자세히 설명해 주세요") ||
        answer.contains("오타가 포함되어 있습니다.") ||
        answer.contains("상황에 따라 다릅니다") ||
        answer.contains("조건을 명확히 설명해 주세요") ||
        answer.contains("현재 답변을 생성할 수 없습니다") ||
        answer.contains("코드의 어떤 부분이 궁금한지 설명해 주세요") ||
        answer.contains("질문이 있다면 알려주세요") ||
        answer.contains("구체적인 질문을 해주시면 도움을 드리겠습니다"));
  }
}
