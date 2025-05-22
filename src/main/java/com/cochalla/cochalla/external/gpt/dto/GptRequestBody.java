package com.cochalla.cochalla.external.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GptRequestBody {
    private String model;
    private Double temperature;
    private Integer max_tokens;
    private Object tool_choice;
    private List<GptTool> tools;
    private List<GptMessage> messages;
}
