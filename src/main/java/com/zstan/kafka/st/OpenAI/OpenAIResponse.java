package com.zstan.kafka.st.OpenAI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
public class OpenAIResponse {
    private List<Choice> choices;
    private ErrorDetail error;

    @Data
    public static class Choice {
        private Message message;

        @Data
        public static class Message {
            private String role;
            private String content;
        }
    }

    @Data
    public static class ErrorDetail {
        private String message;
        private int code;
    }
}