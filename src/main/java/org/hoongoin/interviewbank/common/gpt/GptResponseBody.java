package org.hoongoin.interviewbank.common.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GptResponseBody {
    private String id;
    private String object;
    private long created;
    private List<Choice> choices;
    private Usage usage;

    @Getter
    @Setter
    public static class Choice {
        private int index;
        private Message message;
        @JsonProperty("finish_reason")
        private String finishReason;

        @Getter
        @Setter
        public static class Message {
            private String role;
            private String content;
        }
    }

    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
}
