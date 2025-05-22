package com.cochalla.cochalla.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.cochalla.cochalla.dto.ChatAskRequestDto;
import com.cochalla.cochalla.dto.ChatListItemDto;
import com.cochalla.cochalla.dto.ChatMessageDto;
import com.cochalla.cochalla.dto.ChatMessageResponseDto;
import com.cochalla.cochalla.service.ChatService;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  @GetMapping("/list")
  public ResponseEntity<List<ChatListItemDto>> getUserChatList(
      @AuthenticationPrincipal UserDetails userDetails) {
    String userId = userDetails.getUsername();
    List<ChatListItemDto> response = chatService.chatList(userId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/list/{chatId}")
  public ResponseEntity<ChatMessageResponseDto> getChatMessages(
      @PathVariable Integer chatId,
      @AuthenticationPrincipal UserDetails userDetails) {
    String userId = userDetails.getUsername();
    ChatMessageResponseDto messages = chatService.getMessagesInChat(userId, chatId);
    return ResponseEntity.ok(messages);
  }

  @PostMapping("/ask")
  public ResponseEntity<ChatMessageDto> postQuestion(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody ChatAskRequestDto requestDto) {
    String userId = userDetails.getUsername();
    ChatMessageDto messages = chatService.askQuestion(userId,
        requestDto.getChatId(), requestDto.getQuestion());
    return ResponseEntity.ok(messages);
  }

}
