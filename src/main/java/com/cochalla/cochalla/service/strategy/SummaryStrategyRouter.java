package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.dto.QuestionAnswerPairDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummaryStrategyRouter {

    private final SimpleSummaryStrategy simpleSummaryStrategy;
    private final StructuredSummaryStrategy structuredSummaryStrategy;

    public void summarize(Chat chat, List<QuestionAnswerPairDto> qaList) {
        if (qaList.size() <= 5) {
            log.info("👉 SimpleSummaryStrategy 실행 (QA 개수: {})", qaList.size());
            simpleSummaryStrategy.generate(chat, qaList);
        } else {
            log.info("👉 StructuredSummaryStrategy 실행 (QA 개수: {})", qaList.size());
            structuredSummaryStrategy.generate(chat, qaList);
        }
    }

}
