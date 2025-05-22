package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.dto.QuestionAnswerPairDto;

import java.util.List;

public interface SummaryStrategy {
    void generate(Chat chat, List<QuestionAnswerPairDto> qaList);
}
