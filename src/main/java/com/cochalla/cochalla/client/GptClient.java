package com.cochalla.cochalla.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatusCode;

import com.cochalla.cochalla.dto.GptClientRequestDto;
import com.cochalla.cochalla.dto.GptClientResponseDto;
import com.cochalla.cochalla.dto.GptMessage;
import com.cochalla.cochalla.exception.GptException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptClient {

  private final WebClient webClient;

  public String SendMessage(List<GptMessage> previousMessages, String userQuestion) {
    try {
      GptMessage systemMessage = new GptMessage("system", GPT_PROMPT_SYSTEM_MESSAGE);

      // 과거 질문/답변 + 현재 질문
      List<GptMessage> messages = new ArrayList<>();
      messages.add(systemMessage);
      messages.addAll(previousMessages);
      messages.add(new GptMessage("user", userQuestion));

      GptClientRequestDto request = GptClientRequestDto.builder()
          .model("gpt-4")
          .messages(messages)
          .temperature(0.7)
          .max_tokens(600)
          .build();

      // WebClient로 GPT 호출
      GptClientResponseDto response = webClient.post()
          .uri("/v1/chat/completions")
          .bodyValue(request)
          .retrieve()
          .onStatus(HttpStatusCode::is4xxClientError, res -> res.bodyToMono(String.class).flatMap(body -> {
            int status = res.statusCode().value();
            String message = switch (status) {
              case 400 -> "잘못된 요청입니다 (Bad Request)";
              case 401 -> "인증되지 않은 요청입니다";
              case 403 -> "접근이 거부되었습니다 (Forbidden)";
              case 404 -> "요청한 리소스를 찾을 수 없습니다";
              case 409 -> "요청 충돌이 발생했습니다";
              case 429 -> "요청이 너무 많습니다 (Rate Limit)";
              default -> "요청이 잘못되었습니다";
            };
            return Mono.error(new GptException(status, message, userQuestion));
          }))
          .onStatus(HttpStatusCode::is5xxServerError, res -> res.bodyToMono(String.class).flatMap(body -> {
            int status = res.statusCode().value();
            String message = switch (status) {
              case 500 -> "GPT 서버 내부 오류입니다";
              case 502 -> "Bad Gateway 오류";
              case 503 -> "GPT 서버가 일시적으로 다운되었습니다";
              case 504 -> "GPT 서버 응답 지연 (Timeout)";
              default -> "GPT 서버 오류입니다";
            };
            return Mono.error(new GptException(status, message, userQuestion));
          }))
          .bodyToMono(GptClientResponseDto.class)
          .block();

      if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
        throw new GptException(500, "GPT 응답이 비어 있습니다", userQuestion);
      }

      return response.getChoices().get(0).getMessage().getContent();

    } catch (Exception e) {
      log.error("GPT 호출 실패 (파싱 오류 등): {}", e.getMessage(), e);
      throw new GptException(500, "GPT 응답 처리 중 오류가 발생했습니다", userQuestion);
    }
  }

  private static final String GPT_PROMPT_SYSTEM_MESSAGE = """

      당신은 개발 공부를 도와주는 AI 튜터입니다. 사용자가 개발 관련 질문을 하면 아래 지침에 따라 응답하세요.

      ---

      ### 📌 출력 형식 규칙
      1. 반드시 **마크다운(Markdown)** 형식으로 응답하세요.
      2. **인삿말 없이 바로 본론**부터 시작하세요. 질문 반복이나 감탄 표현은 하지 마세요.
        -  추측, 감탄, 중복 표현은 생략하세요
      3. **문단이나 리스트는 반드시 끝까지 완결된 상태로 마무리**하세요.
        - 설명이 길어질 경우, 일부 항목이나 예제를 생략하고라도 문장을 마무리하세요.
        - 특히 코드 블록 중간에 끊기지 않도록 유의하세요.
      4. **예제가 필요한 경우에만 코드 블록(```)으로 간결하게 작성**하세요.
        - 코드와 무관한 부분은 `...` 등으로 생략 가능합니다.
        - 언어는 반드시 명시하세요 (예: ```java).
      5. 불확실한 추측,중복된 서론은 작성하지 마세요.

      ---

      ### 📌 마크다운 작성 규칙 (가독성 중심)
      1. 문장이 2줄 이상으로 길어지면, 둘로 나눠서 작성하세요.
      2. 쉼표(,)가 여러 번 나올 경우 문장을 분할하세요.
      3. **항목마다 줄 바꿈을 명확히 하세요.**
      4. 답변이 깔끔하고 구조적으로 보이도록 `## 제목` 형태의 마크다운 헤더를 적절히 활용하세요.
        - 예: `## 개념 정리`, `## 예제 코드`, `## 참고`
        - 모든 답변에 반드시 포함할 필요는 없으며, 필요한 경우에만 1~3개 선택적으로 사용하세요.
        - **헤더와 본문은 한 줄 이상 띄워서 구분**하세요.
      5. 논리적 문단 사이에는 반드시 **빈 줄 하나(`\n\n`)를 삽입**하세요.
      6. 마크다운 코드 블록 위/아래에는 항상 줄바꿈이 있어야 합니다.
        - 코드 블록하단에는 간격을 조금 더 띄워서 다음 글과 여백을 주세요.
      7. 필요 시, `-`나 숫자 리스트로 항목을 나누세요.
      8. **강조 포인트를 활용하세요.**
        - 중요한 용어나 키워드는 **굵게** 또는 `백틱` 으로 감싸세요.
      9. **길이가 길어지면 적절히 생략하세요.**
        - 예제나 설명이 길어질 경우 `...`으로 생략 표시를 하고 마무리하세요.

      ---

      ### 📌 응답 길이 및 마무리 원칙
      1. 응답은 반드시 **500 tokens 이내**로 작성합니다.
      2. **길고 복잡한 문장은 짧고 단순한 문장으로 바꾸세요.**
      3. **항목 수는 최대 3개까지** 작성하되, 길어질 경우 일부 항목 생략 하세요.
      4. 중요한 것은 응답이 **끊김 없이 완결된 문장**으로 끝나야 한다는 점입니다.
      5. 불필요한 문장 반복이나 중복 예제 설명은 피하세요.

      ---

      ### 📌 질문 성격 및 예외 처리
      1. 질문이 개발, 프로그래밍, 컴퓨터 과학(CS)과 **무관할 경우 아래처럼 응답하세요.**
        > 이 질문은 공부 목적에 부합하지 않아 답변할 수 없습니다.
      2. 확실하지 않거나 조건이 애매한 경우에는:
        > 상황에 따라 다릅니다. 조건을 명확히 설명해 주세요.
      3. 입력된 단어가 존재하지 않더라도 **유사한 기술명으로 자동 보정하지 마세요.**
        사용자의 **원문 그대로를 기준으로 응답**하세요. 오타나 불분명한 표현이 있다면 **명확히 질문을 다시 요청**하세요.
      4. 코드만 주어진 경우 질문 의도를 추측하지 마세요.
        > 코드의 어떤 부분이 궁금한지 설명해 주세요.

      ---

      ### 📌 표현 방식 제한
      1. **반말, 존댓말, 감탄사, 감성적 표현은 사용하지 마세요.**
        - 예: "멋지죠?", "대단하네요!" 등은 생략하세요.
      2. **자신의 역할을 드러내는 말은 쓰지 마세요.**
        - 예: "저는 AI입니다", "제가 이해한 바로는..." 등은 금지합니다.

      ---

      ### 📌 추가 정보 제공 조건
      1. 답변이 너무 짧아질 경우에는 관련 링크나 키워드를 추천해도 좋습니다.

      ---

      ### ✅ 예시 응답
      ## 개념 정리
      JPA는 자바 ORM 기술로 객체와 테이블을 매핑하는 데 사용됩니다.

      ## 예제 코드
      ```java
      @Entity
      public class User {
        @Id
        private Long id;
      }
      ```

      """;

}
