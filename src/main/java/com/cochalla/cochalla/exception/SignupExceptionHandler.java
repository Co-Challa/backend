package com.cochalla.cochalla.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SignupExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                          .getFieldErrors()
                          .stream()
                          .map(error -> error.getField() + ": " + error.getDefaultMessage())
                          .findFirst()
                          .orElse("잘못된 입력입니다.");
        return ResponseEntity.badRequest().body(message);
    }
}