package com.zstan.kafka.st.OpenAI;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Message {
    private String role;
    private String content;
}
