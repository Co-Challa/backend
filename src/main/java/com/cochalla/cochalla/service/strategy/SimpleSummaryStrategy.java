package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.dto.GptSummaryResponseDto;
import com.cochalla.cochalla.dto.QuestionAnswerPairDto;
import com.cochalla.cochalla.external.gpt.GptClient;
import com.cochalla.cochalla.external.gpt.GptRequestBuilder;
import com.cochalla.cochalla.repository.PostRepository;
import com.cochalla.cochalla.repository.SummaryRepository;
import com.cochalla.cochalla.service.PostService;
import com.cochalla.cochalla.service.SummaryStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SimpleSummaryStrategy extends AbstractSummaryStrategy {

    private final GptClient gptClient;

    public SimpleSummaryStrategy(
            SummaryRepository summaryRepository,
            PostRepository postRepository,
            GptClient gptClient,
            SummaryStatusService summaryStatusService,
            PostService postService
    ) {
        super(summaryRepository, postRepository, summaryStatusService, postService);
        this.gptClient = gptClient;
    }

    @Override
    public void generate(Chat chat, List<QuestionAnswerPairDto> qaList) {
        try {
            String qaText = buildSimpleSummary(qaList);
            String systemPrompt = GptRequestBuilder.buildSystemPromptForQa();
            GptSummaryResponseDto response = gptClient.requestSummaryWithFunctionCall(systemPrompt, qaText);
            saveSummary(chat, response);
        } catch (Exception e) {
            log.error("GPT 요약 실패 - ChatId: {}, UserId: {}", chat.getChatId(), chat.getUser().getUserId(), e);
        }
    }

    private String buildSimpleSummary(List<QuestionAnswerPairDto> qaList) {
        StringBuilder sb = new StringBuilder();
        for (QuestionAnswerPairDto qa : qaList) {
            sb.append("질문: ").append(qa.getQuestion()).append("\n");
            sb.append("답변: ").append(qa.getAnswer()).append("\n\n");
        }
        return sb.toString();
    }
}
