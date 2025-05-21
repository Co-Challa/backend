package com.cochalla.cochalla.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
  @Value("${gpt.api.key}")
  private String openAiKey;

  @Bean
  public WebClient gptWebClient(
      @Value("${gpt.api.base-url}") String baseUrl,
      @Value("${gpt.api.key}") String apiKey) {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

}
