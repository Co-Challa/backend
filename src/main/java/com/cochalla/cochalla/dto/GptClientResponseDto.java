package com.cochalla.cochalla.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptClientResponseDto {
  private List<Choice> choices;

  @Getter
  @NoArgsConstructor
  public static class Choice {
    private GptMessage message;
  }

  public String getContent() {
    return choices != null && !choices.isEmpty()
        ? choices.get(0).getMessage().getContent()
        : null;
  }
}
