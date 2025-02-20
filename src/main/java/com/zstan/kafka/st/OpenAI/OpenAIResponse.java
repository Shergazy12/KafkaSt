package com.zstan.kafka.st.OpenAI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenAIResponse {
    private List<Choice> choices;
    private ErrorDetail error;

    // Явные геттеры (если Lombok не сгенерировал)
    public List<Choice> getChoices() {
        return choices;
    }

    public ErrorDetail getError() {
        return error;
    }

    @Data
    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }
    }

    @Data
    public static class Message {
        private String role;
        private String content;

        public String getContent(){
            return content;
        }
    }

    @Data
    static class ErrorDetail {
        private String message;
        private int code;

        public int getCode() {
            return code;
        }
    }
}

