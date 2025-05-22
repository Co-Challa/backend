package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.domain.Summary;
import com.cochalla.cochalla.dto.GptSummaryResponseDto;
import com.cochalla.cochalla.repository.PostRepository;
import com.cochalla.cochalla.repository.SummaryRepository;
import com.cochalla.cochalla.service.SummaryStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSummaryStrategy implements SummaryStrategy{

    protected final SummaryRepository summaryRepository;
    protected final PostRepository postRepository;
    protected final SummaryStatusService summaryStatusService;

    protected void saveSummary(Chat chat, GptSummaryResponseDto response) {

        Optional<Summary> optional = summaryRepository.findByChat(chat);

        Summary summary;
        if (optional.isPresent()) {
            summary = optional.get();
            // 성공 요약이 이미 있으면(타이틀/컨텐츠 모두 null 아님) 저장하지 않음
            if (summary.getTitle() != null && summary.getContent() != null) {
                log.warn("성공 Summary가 이미 존재하여 덮어쓰지 않습니다. ChatId: {}", chat.getChatId());
                return;
            }
            // 실패 Summary만 있으면 응답 데이터로 업데이트
            if (response != null) {
                summary.updateFromResponse(response);
            }
        } else {
            // Summary가 아예 없는 경우 새로 생성
            summary = Summary.of(
                    chat.getUser(),
                    chat,
                    response != null ? response.getTitle() : null,
                    response != null ? response.getContent() : null
            );
        }

        // **먼저 summary를 저장해줘야 함!**
        summaryRepository.save(summary);

        Optional<Post> postOpt = postRepository.findBySummary(summary);

        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setSummary(summary);
            postRepository.save(post); // 변경 반영
        } else {
            log.error("[치명적] Summary에 연결된 Post가 없습니다! summaryId: {}, chatId: {}", summary.getSummaryId(), chat.getChatId());
            throw new IllegalStateException("요약 저장 실패: Summary에 연결된 Post가 없습니다.");
        }

        // Redis 상태 동기화
        summaryStatusService.setSuccess(chat.getUser().getUserId());
        log.info("Summary 저장/업데이트 및 Post 연결 성공, ChatId: {}, SummaryId: {}", chat.getChatId(), summary.getSummaryId());

    }
}
