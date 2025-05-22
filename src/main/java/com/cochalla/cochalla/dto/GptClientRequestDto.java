package com.cochalla.cochalla.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GptClientRequestDto {
  private String model;
  private List<GptMessage> messages;
  private double temperature;
  private int max_tokens;

  public static GptClientRequestDto withSystem(String systemMessage, String userMessage) {
    return GptClientRequestDto.builder()
        .model("gpt-4-turbo")
        .messages(List.of(
            new GptMessage("system", systemMessage),
            new GptMessage("user", userMessage)))
        .temperature(0.7)
        .max_tokens(600)
        .build();
  }
}
