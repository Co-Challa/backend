package com.cochalla.cochalla.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException e) {
    log.warn("🔍 리소스를 찾을 수 없음: {}", e.getMessage());
    return errorResponse(404, e.getMessage());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<Map<String, String>> handleForbidden(ForbiddenException e) {
    log.warn("🚫 접근 권한 없음: {}", e.getMessage());
    return errorResponse(403, e.getMessage());
  }

  private ResponseEntity<Map<String, String>> errorResponse(int status, String message) {
    Map<String, String> body = new HashMap<>();
    body.put("상태코드", String.valueOf(status)); // "status" → "상태코드"
    body.put("메시지", message); // "message" → "메시지"
    return ResponseEntity.status(status).body(body);
  }
}
