package com.cochalla.cochalla.exception;

import lombok.Getter;

@Getter
public class GptException extends RuntimeException {
  private final int statusCode;
  private final String userMessage;
  private final String question;

  public GptException(int statusCode, String userMessage, String question) {
    super(userMessage);
    this.statusCode = statusCode;
    this.userMessage = userMessage;
    this.question = question;
  }

}