package com.cochalla.cochalla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionAnswerPairDto {
    private final String question;
    private final String answer;
}
