package com.cochalla.cochalla.external.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GptMessage {
    private String role;
    private String content;
}
