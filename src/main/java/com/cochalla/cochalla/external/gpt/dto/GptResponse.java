package com.cochalla.cochalla.external.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class GptResponse {

    private List<Choice> choices;

    @Getter
    public static class Choice {
        private Message message;
    }

    @Getter
    public static class Message {
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;
    }

    @Getter
    public static class ToolCall {
        private Function function;
    }

    @Getter
    public static class Function {
        private String arguments;
    }

    public String getToolCallArguments() {
        return choices.get(0).getMessage().getToolCalls().get(0).getFunction().getArguments();
    }
}
