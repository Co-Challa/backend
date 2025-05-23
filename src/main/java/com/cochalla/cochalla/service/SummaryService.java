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

    public void retrySummary(Summary summary) {
        if (summary.getTitle() != null && summary.getContent() != null) {
            throw new IllegalStateException("이미 성공한 Summary는 재요약할 수 없습니다.");
        }

        summaryStatusService.setPending(summary.getUser().getUserId());
        List<QuestionAnswerPairDto> qaList = qaService.getQAPairs(summary.getChat());
        summaryStrategyRouter.summarize(summary.getChat(), qaList);

    }
}
