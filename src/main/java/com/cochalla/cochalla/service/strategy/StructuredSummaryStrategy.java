package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.dto.GptSummaryResponseDto;
import com.cochalla.cochalla.dto.QuestionAnswerPairDto;
import com.cochalla.cochalla.external.gpt.GptClient;
import com.cochalla.cochalla.external.gpt.GptRequestBuilder;
import com.cochalla.cochalla.repository.SummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StructuredSummaryStrategy extends AbstractSummaryStrategy {

    private final GptClient gptClient;

    public StructuredSummaryStrategy(SummaryRepository summaryRepository, GptClient gptClient) {
        super(summaryRepository);
        this.gptClient = gptClient;
    }

    @Override
    public void generate(Chat chat, List<QuestionAnswerPairDto> qaList) {
        List<String> miniSummaries = gptClient.requestMiniSummaries(qaList);

        String summaryText = String.join("\n", miniSummaries);
        String systemPrompt = GptRequestBuilder.buildSystemPromptForSummary();

        GptSummaryResponseDto response = gptClient.requestSummaryWithFunctionCall(systemPrompt, summaryText);

        saveSummary(chat,response);
    }
}
