package com.cochalla.cochalla.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
  private Integer chatId;
  private List<ChatMessageDto> messageList; // (질문/답변)
  private boolean writable; // 질문 가능 여부

}
