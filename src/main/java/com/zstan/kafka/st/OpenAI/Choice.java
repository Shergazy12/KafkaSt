package com.zstan.kafka.st.OpenAI;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@Getter
@Setter
public class Choice {
    private Message message;
}
