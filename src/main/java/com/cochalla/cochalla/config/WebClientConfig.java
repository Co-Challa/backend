package com.cochalla.cochalla.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Value("${openai.api.url}")
  private String baseUrl;

  @Value("${openai.api.key}")
  private String apiKey;

  @Bean
  public WebClient GptAiClient() {
    return WebClient.builder()
        .baseUrl(baseUrl) // 예: https://api.openai.com
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}