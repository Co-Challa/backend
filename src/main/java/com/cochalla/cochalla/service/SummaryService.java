package com.cochalla.cochalla.service;

import com.cochalla.cochalla.domain.*;
import com.cochalla.cochalla.dto.QuestionAnswerPairDto;
import com.cochalla.cochalla.repository.*;
import com.cochalla.cochalla.service.strategy.SummaryStrategyRouter;
import com.cochalla.cochalla.util.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SummaryService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final QuestionRepository questionRepository;
    private final SummaryRepository summaryRepository;
    private final RedisLockService redisLockService;
    private final QaService qaService;
    private final SummaryStrategyRouter summaryStrategyRouter;
    private final SummaryStatusService summaryStatusService;

    public void generateSummariesForHour(int hour) {
        LocalDate today = LocalDate.now();

        List<User> targetUsers = userRepository.findTargetUsersForSummary(hour, today);
        log.info("[{}시] 요약 대상 유저 수: {}", hour, targetUsers.size());

        for (User user : targetUsers) {
            String lockKey = "summary:lock" + user.getUserId();
            boolean locked = redisLockService.acquireLock(lockKey, Duration.ofMinutes(10));

            if (!locked) {
                log.info("유저({})는 이미 요약 처리 중 -> 생략", user.getUserId());
                continue;
            }

            summaryStatusService.setPending(user.getUserId());

            Optional<Chat> latestChatOpt = chatRepository.findTopByUser_UserIdOrderByCreatedAtDesc(user.getUserId());
            if (latestChatOpt.isEmpty()) {
                log.info("유저({})의 채팅 없음 -> 요약 생략", user.getUserId());
                continue;
            }

            Chat latestChat = latestChatOpt.get();
            boolean summaryExists = summaryRepository.existsByUserAndChat(user, latestChat);
            if (summaryExists) {
                log.info("유저({})의 최신 채팅({})은 이미 요약됨 -> 생략", user.getUserId(), latestChat.getChatId());
                continue;
            }

            List<Question> questions = questionRepository.findByChat_ChatIdOrderByCreatedAtAsc(latestChat.getChatId());
            if (questions.isEmpty()) {
                log.info("유저({})의 질문 없음 -> 요약 생략", user.getUserId());
                continue;
            }

            /*
             * <<NOTICE>> - user.markSummaryAsDone();
             * JPA 변경 감지(Persistence Context)에 의해 lastSummaryDate가 자동 반영됨
             * 명시적 save(user)는 생략 가능 (단, 트랜잭션 범위 내에 있어야 함)
             */

            try {
                List<QuestionAnswerPairDto> qaList = qaService.getQAPairs(latestChat);
                summaryStrategyRouter.summarize(latestChat, qaList);
                user.markSummaryAsDone();
                log.info("요약 저장 완료 - user: {}, chatId: {}", user.getUserId(), latestChat.getChatId());
                summaryStatusService.setSuccess(user.getUserId());
            } catch (Exception e) {
                summaryStatusService.setFailed(user.getUserId());
                log.warn("요약 실패 - user: {}, chatId: {}", user.getUserId(), latestChat.getChatId(), e);
            } finally {
                redisLockService.releaseLock(lockKey);
            }
        }
    }

    public void saveEmptySummary(User user, Chat chat) {
        Optional<Summary> optional = summaryRepository.findByChat(chat);

        if (optional.isPresent()) {
            Summary summary = optional.get();
            // 이미 성공 요약이 있으면(타이틀/컨텐츠가 모두 null이 아님) 아무것도 하지 않음
            if (summary.getTitle() != null && summary.getContent() != null) {
                log.warn("성공 Summary가 이미 존재하여 실패 요약 저장을 생략합니다. ChatId: {}", chat.getChatId());
                return;
            }
            // 실패 요약이 이미 있다면, 중복 저장 방지(원하면 로그만)
            log.warn("빈 Summary가 이미 존재하여 중복 저장을 생략합니다. ChatId: {}", chat.getChatId());
            return;
        }

        // Summary가 아예 없는 경우에만 실패 Summary 저장
        Summary summary = Summary.of(user, chat, null, null);
        summaryRepository.save(summary);
        log.info("빈 Summary 저장 (요약 실패) - ChatId: {}, UserId: {}", chat.getChatId(), user.getUserId());

        // Redis 상태 동기화
        summaryStatusService.setFailed(user.getUserId());
    }

    public void retrySummary(Summary summary) {
        // 1. 실패 Summary(즉, title/content가 모두 null)만 재요약 가능
        if (summary.getTitle() != null && summary.getContent() != null) {
            throw new IllegalStateException("이미 성공한 Summary는 재요약할 수 없습니다.");
        }

        // 2. 요약 요청 직전 상태를 PENDING으로 변경 (Redis 동기화)
        summaryStatusService.setPending(summary.getUser().getUserId());

        // 3. 질문-답변 리스트 확보
        List<QuestionAnswerPairDto> qaList = qaService.getQAPairs(summary.getChat());

        // 4. 전략 분기(질문 수 기준)
        summaryStrategyRouter.summarize(summary.getChat(), qaList);

        // 5. (전략 내부에서 성공/실패 Redis 상태 관리, Summary 저장까지 모두 처리됨)
    }

    public void testGenerateSummary(Chat chat) {
        // 질문-답변 페어 리스트를 확보
        List<QuestionAnswerPairDto> qaList = qaService.getQAPairs(chat);

        // 전략 분기는 Router가 알아서 처리!
        summaryStrategyRouter.summarize(chat, qaList);
    }
}
