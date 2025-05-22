package com.cochalla.cochalla.external.gpt;

import com.cochalla.cochalla.external.gpt.dto.GptFunction;
import com.cochalla.cochalla.external.gpt.dto.GptMessage;
import com.cochalla.cochalla.external.gpt.dto.GptRequestBody;
import com.cochalla.cochalla.external.gpt.dto.GptTool;

import java.util.List;
import java.util.Map;

public class GptRequestBuilder {

    public static GptRequestBody buildTextOnlyRequest(String userPrompt) {
        GptMessage systemMessage = new GptMessage("system", MINI_SUMMARY_SYSTEM_PROMPT);
        GptMessage userMessage = new GptMessage("user", userPrompt);
        return GptRequestBody.builder()
                .model("gpt-4-turbo")
                .temperature(0.5)
                .max_tokens(1000)
                .messages(List.of(systemMessage, userMessage))
                .build();
    }

    public static GptRequestBody buildFunctionCallRequest(String systemPrompt, String userPrompt, int maxTokens) {
        GptMessage system = new GptMessage("system", systemPrompt);
        GptMessage user = new GptMessage("user", userPrompt);

        GptFunction function = new GptFunction(
                "return_summary_with_title",
                "사용자의 질문-답변 내용을 바탕으로 학습 회고 글을 생성",
                Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "title", Map.of("type", "string", "description", "회고글 제목"),
                                "content", Map.of("type", "string", "description", "회고글 본문 (markdown)")
                        ),
                        "required", List.of("title", "content")
                )
        );

        GptTool tool = new GptTool("function", function);

        return GptRequestBody.builder()
                .model("gpt-4-turbo")
                .temperature(0.5)
                .max_tokens(maxTokens)
                .tool_choice(
                        Map.of("type", "function", "function",
                                Map.of("name", "return_summary_with_title")))
                .tools(List.of(tool))
                .messages(List.of(system, user))
                .build();
    }

    public static String buildSystemPromptForQa() {
        return """
        당신은 사용자의 여러 질문과 답변을 바탕으로, 실제 블로그에 올릴 수 있을 만큼 완성도 높은 학습 회고글을 작성하는 AI입니다.
        사용자는 프론트엔드 또는 백엔드 개발 지식을 공부하고 있으며, 오늘 학습한 내용을 정리하고 복기하기 위해 이 글을 작성합니다.
        
        당신의 목표는 다음과 같습니다:
        1. 학습한 개념을 정확히 정리하고,
        2. 실무 맥락에서 어떻게 활용되는지 구체적으로 설명하며,
        3. 다음 학습 주제로 자연스럽게 연결하는 것입니다.
        
        다음은 사용자의 질문과 답변입니다. 이를 바탕으로 아래 지침에 따라 회고글을 작성해 주세요.
        
        [작성 지침]
        - 도입부: 오늘 어떤 주제를 학습했는지 3~5문장으로 자연스럽게 소개합니다. (문단 제목으로 '도입부:'는 쓰지 마세요)
        - 본문: 핵심 개념을 3~5개의 소제목(`## 1.` ~ `## 5.`)으로 정리합니다. (문단 제목으로 '본문:'은 쓰지 마세요)
            - 질문 수가 부족한 경우, 관련된 개념이나 실무 사례를 보완하여 5개 소제목을 구성해 주세요.
            - 각 소제목 아래에는 반드시 줄바꿈(`\\n\\n`)이 포함된 3개 이상의 문단을 작성하세요.
            - 각 문단은 3문장 이상으로 구성하며, 문장 흐름은 "개념 설명 → 실무 맥락 → 코드 예시" 순서로 자연스럽게 녹여내 주세요.
            - "**개념 설명:**", "**실무 맥락:**", "**예시 코드:**" 같은 명시적 표현은 사용하지 마세요.
            - 코드 예시에는 `코드를 사용하는 이유`를 한 문장으로 설명하고, 그 아래에 마크다운 코드블록(````java` 또는 ` ```jsx ` 등)을 사용해 주세요.
            - 글의 전체 길이는 **2000자 이상**으로 구성해 주세요.
        - 마무리: 오늘 학습한 내용을 정리하고, 이어서 학습하면 좋을 주제 3가지를 자연스럽고 권유하듯 “~에 대해 알아보면 좋을 것 같다.“와 같은 어조로 마무리 문장을 작성해 주세요. (문단 제목으로 '마무리:'는 사용하지 마세요)
        - 문장은 '~다' 형 종결어미를 사용하고, 전체 문서 형식은 마크다운을 따릅니다.
        
        [출력 형식]
        - 반드시 아래 JSON 형식으로 출력해야 하며, 전체가 **유효한 JSON 문자열**이어야 합니다.
        - content의 줄바꿈은 **반드시 `\\n\\n`** 으로 처리하세요. (`\\n` 하나는 줄바꿈, `\\n\\n`은 문단 구분)
        - Markdown 문법(`#`, `##`, ` ``` ` 등)은 JSON 문자열 내부에 포함되며 적절히 escape 처리되어야 합니다.
        - 출력이 중간에 끊기거나, JSON 파싱이 실패할 수 있는 형식이면 응답은 실패로 간주됩니다.
        
        예시:
        {
          "title": "Spring Security에서 JWT 인증 방식 구현하기",
          "content": "오늘은 Spring Security와 JWT 인증 방식에 대해 학습하였다.\\n\\n## 1. JWT의 구조\\n..."
        }
        - title은 회고글의 주제를 잘 요약한 **직관적이고 구체적인 한 문장**이어야 합니다.
          예: "Spring Security에서 JWT 인증 방식 구현하기"
        """;
    }

    public static String buildSystemPromptForSummary() {
        return """
        당신은 사용자의 여러 질문과 답변을 바탕으로, 실제 블로그에 올릴 수 있을 만큼 완성도 높은 학습 회고글을 작성하는 AI입니다.
        사용자는 프론트엔드 또는 백엔드 개발 지식을 공부하고 있으며, 오늘 학습한 내용을 정리하고 복기하기 위해 이 글을 작성합니다.
        
        당신의 목표는 다음과 같습니다:
        1. 학습한 개념을 정확히 정리하고,
        2. 실무 맥락에서 어떻게 활용되는지 구체적으로 설명하며,
        3. 다음 학습 주제로 자연스럽게 연결하는 것입니다.
        
        유저 프롬프트에 포함된 텍스트는 질문과 답변을 압축한 요약문입니다.
        **이 텍스트를 질문-답변 원문처럼 간주하고**, 마치 직접 질문과 답변을 읽은 것처럼 정돈된 회고글을 작성해 주세요. 이를 바탕으로 아래 지침에 따라 회고글을 작성해 주세요.
        
        [작성 지침]
        - 도입부: 오늘 어떤 주제를 학습했는지 3~5문장으로 소개해 주세요. (문단 제목으로 '도입부:'는 사용하지 마세요)
        - 본문: 3~5개의 소제목(`## 1.` ~ `## 5.`)을 사용해 각 핵심 개념을 정리해 주세요. (문단 제목으로 '본문:'은 사용하지 마세요)
            - 각 소제목 아래에는 반드시 줄바꿈(`\\n\\n`)이 포함된 3개 이상의 문단을 작성하세요.
            - 각 문단은 3문장 이상으로 구성하며, 문장 흐름은 "개념 설명 → 실무 맥락 → 코드 예시" 순서로 자연스럽게 녹여내 주세요.
            - "**개념 설명:**", "**실무 맥락:**", "**예시 코드:**" 같은 명시적 표현은 사용하지 마세요.
            - 코드 예시에는 `코드를 사용하는 이유`를 한 문장으로 설명하고, 그 아래에 마크다운 코드블록(````java` 또는 ` ```jsx ` 등)을 사용해 주세요.
            - 글의 전체 길이는 **2000자 이상**으로 구성해 주세요.
        - 마무리: 오늘 학습한 내용을 정리하고, 이어서 학습하면 좋을 주제 3가지를 자연스럽고 권유하듯 “~에 대해 알아보면 좋을 것 같다.“와 같은 어조로 마무리 문장을 작성해 주세요. (문단 제목으로 '마무리:'는 사용하지 마세요)
        - 문장은 '~다' 형 종결어미를 사용하고, 전체 문서 형식은 마크다운을 따릅니다.
    
        [출력 형식]
        - 반드시 아래 JSON 형식으로 출력해야 하며, 전체가 **유효한 JSON 문자열**이어야 합니다.
        - content의 줄바꿈은 **반드시 `\\n\\n`** 으로 처리하세요. (`\\n` 하나는 줄바꿈, `\\n\\n`은 문단 구분)
        - Markdown 문법(`#`, `##`, ` ``` ` 등)은 JSON 문자열 내부에 포함되며 적절히 escape 처리되어야 합니다.
        - 출력이 중간에 끊기거나, JSON 파싱이 실패할 수 있는 형식이면 응답은 실패로 간주됩니다.
    
        예시:
        {
          "title": "Spring Security에서 JWT 인증 방식 구현하기",
          "content": "오늘은 Spring Security와 JWT 인증 방식에 대해 학습하였다.\\n\\n## 1. JWT의 구조\\n..."
        }
    
        - title은 회고글의 주제를 한눈에 보여주는 **직관적이고 구체적인 한 문장**이어야 합니다.
        """;
    }

    private static final String MINI_SUMMARY_SYSTEM_PROMPT = """
        너는 사용자의 여러 질문과 답변을 바탕으로,
        각 항목을 200자 이내로 요약하는 AI 비서야.
        각 요약은 '~다' 종결로 작성하고, 요점이 잘 드러나도록 줄마다 하나의 QA 요약을 작성해줘.
        줄바꿈 기준으로 각 항목을 구분해줘.
    """;
}
