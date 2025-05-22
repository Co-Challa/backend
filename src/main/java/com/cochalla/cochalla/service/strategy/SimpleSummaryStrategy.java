package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.dto.GptSummaryResponseDto;
import com.cochalla.cochalla.dto.QuestionAnswerPairDto;
import com.cochalla.cochalla.external.gpt.GptClient;
import com.cochalla.cochalla.external.gpt.GptRequestBuilder;
import com.cochalla.cochalla.repository.SummaryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleSummaryStrategy extends AbstractSummaryStrategy {

    private final GptClient gptClient;

    public SimpleSummaryStrategy(SummaryRepository summaryRepository, GptClient gptClient) {
        super(summaryRepository);
        this.gptClient = gptClient;
    }

    @Override
    public void generate(Chat chat, List<QuestionAnswerPairDto> qaList) {
        String qaText = buildSimpleSummary(qaList);
        String systemPrompt = GptRequestBuilder.buildSystemPromptForQa();
        GptSummaryResponseDto response = gptClient.requestSummaryWithFunctionCall(systemPrompt, qaText);
        saveSummary(chat, response);
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
