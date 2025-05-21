package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageDto {
  private Integer id; // 질문,답변의 Id
  private String role; // "user" or "assistant"
  private String content; // 질문,답변 내용
  private LocalDateTime timestamp; // 질문,답변 시간
  private Integer questionId; // user: null , assistant : 질문Id
  private boolean saved;

}