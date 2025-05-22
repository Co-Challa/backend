package com.cochalla.cochalla.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cochalla.cochalla.controller.ChatController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(assignableTypes = ChatController.class)
public class GptExceptionHandler {
  @ExceptionHandler(GptException.class)
  public ResponseEntity<Map<String, String>> handleGptException(GptException e) {
    Map<String, String> error = new HashMap<>();
    log.error("❌ GPT 호출 실패: status={}, message={}", e.getStatusCode(), e.getMessage());
    error.put("message", e.getMessage());
    error.put("status", String.valueOf(e.getStatusCode()));
    return ResponseEntity.status(e.getStatusCode()).body(error);
  }

}
