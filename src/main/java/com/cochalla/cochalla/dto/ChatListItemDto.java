package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatListItemDto {
  private Integer chatId;
  private LocalDateTime createdAt;
  private boolean isNew; // 새로 생성된 채팅방 여부
  private String summaryTitle; // 요약보고서 제목
  private boolean isWritable; // 현재 채팅방 질문 가능 여부
}
