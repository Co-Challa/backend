package com.cochalla.cochalla.external.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GptFunction {
    private String name;
    private String description;
    private Map<String, Object> parameters;
}
