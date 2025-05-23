package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.domain.Summary;
import com.cochalla.cochalla.dto.GptSummaryResponseDto;
import com.cochalla.cochalla.repository.PostRepository;
import com.cochalla.cochalla.repository.SummaryRepository;
import com.cochalla.cochalla.service.PostService;
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
    protected final PostService postService;

    protected void saveSummary(Chat chat, GptSummaryResponseDto response) {

        Optional<Summary> optional = summaryRepository.findByChat(chat);

        Summary summary;
        if (optional.isPresent()) {
            summary = optional.get();
            if (summary.getTitle() != null && summary.getContent() != null) {
                log.warn("성공 Summary가 이미 존재하여 덮어쓰지 않습니다. ChatId: {}", chat.getChatId());
                return;
            }
            if (response != null) {
                summary.updateFromResponse(response);
            }
        } else {
            summary = Summary.of(
                    chat.getUser(),
                    chat,
                    response != null ? response.getTitle() : null,
                    response != null ? response.getContent() : null
            );
        }

        summaryRepository.save(summary);

        Optional<Post> postOpt = postRepository.findBySummary(summary);
        if (postOpt.isEmpty()) {
            postService.create(summary);
            log.info("요약에 연결된 Post가 없어 create를 호출해 Post를 생성했습니다. summaryId: {}", summary.getSummaryId());
        } else {
            log.info("이미 연결된 Post가 있습니다. postId: {}", postOpt.get().getPostId());
        }

        summaryStatusService.setSuccess(chat.getUser().getUserId());
        log.info("Summary 저장/업데이트 및 Post 연결 성공, ChatId: {}, SummaryId: {}", chat.getChatId(), summary.getSummaryId());

    }
}
