package com.cochalla.cochalla.external.gpt;

import com.cochalla.cochalla.dto.GptSummaryResponseDto;

import com.cochalla.cochalla.dto.QuestionAnswerPairDto;
import com.cochalla.cochalla.external.gpt.dto.GptRequestBody;
import com.cochalla.cochalla.external.gpt.dto.GptResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
public class GptClient {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder().build();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GptSummaryResponseDto requestSummaryWithFunctionCall(String systemPrompt, String userPrompt) {
        try {
            return requestSummaryWithFunctionCall(systemPrompt, userPrompt, 2000);
        } catch (RuntimeException e) {
            log.warn("⚠️ 기본 토큰 요청 실패, max_tokens 3000으로 재시도합니다.");
            return requestSummaryWithFunctionCall(systemPrompt, userPrompt, 3000);
        }
    }

    public GptSummaryResponseDto requestSummaryWithFunctionCall(String systemPrompt, String userPrompt, int maxTokens) {
        GptRequestBody requestBody = GptRequestBuilder.buildFunctionCallRequest(systemPrompt, userPrompt, maxTokens);

        GptResponse response = webClient.post()
                .uri(apiUrl + "/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GptResponse.class)
                .block();

        return parseFunctionCallResponse(response);
    }

    public List<String> requestMiniSummaries(List<QuestionAnswerPairDto> qaList) {
        String prompt = buildMiniSummaryPrompt(qaList);

        return requestSummary(prompt)
                .lines()
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .toList();
    }

    /**
     * <<NOTICE>> - requestSummary
     * 일반 텍스트 기반 요약 요청을 보냄
     * Function-call이 아닌 단순 content 응답을 받는 구조이며,
     * content는 JSON 파싱 없이 직접 문자열로 추출됨
     */
    public String requestSummary(String prompt) {
        log.info("[GPT 요약 요청 프롬프트]\n{}", prompt);

        GptRequestBody requestBody = GptRequestBuilder.buildTextOnlyRequest(prompt);

        String response = webClient.post()
                .uri(apiUrl + "/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("[GPT 요약 응답]\n{}", response);

        return extractTextResponse(response);
    }

    private String buildMiniSummaryPrompt(List<QuestionAnswerPairDto> qaList) {
        StringBuilder sb = new StringBuilder();
        sb.append("질문과 답변 목록:\n\n");

        for (int i = 0; i < qaList.size(); i++) {
            QuestionAnswerPairDto qa = qaList.get(i);
            sb.append("질문 ").append(i + 1).append(": ").append(qa.getQuestion()).append("\n");
            sb.append("답변 ").append(i + 1).append(": ").append(qa.getAnswer()).append("\n\n");
        }

        return sb.toString();
    }

    private String extractTextResponse(String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            return root.path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (Exception e) {
            throw new RuntimeException("GPT 일반 응답 파싱 실패", e);
        }
    }

    private GptSummaryResponseDto parseFunctionCallResponse(GptResponse response) {
        try {
            String argsText = response.getToolCallArguments();
            return objectMapper.readValue(argsText, GptSummaryResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("GPT function-call 응답 파싱 실패", e);
        }
    }

}
