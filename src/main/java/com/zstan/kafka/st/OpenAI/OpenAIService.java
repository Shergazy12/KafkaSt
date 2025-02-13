package com.zstan.kafka.st.OpenAI;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class OpenAIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public String getGPTResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String requestBody = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage.substring(0, Math.min(userMessage.length(), 1000)) + "\"}],"
                + "\"temperature\": 0.7"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, OpenAIResponse.class);

        OpenAIResponse openAIResponse = response.getBody();
        //System.out.println("Ответ от API: " + response.getBody());

        if (openAIResponse != null) {
            if (openAIResponse.getError() != null && openAIResponse.getError().getCode() == 402) {
                return "Извините, но для выполнения этого запроса недостаточно кредитов. " +
                        "Пожалуйста, сократите длину сообщения или попробуйте позже.";
            }

            if (openAIResponse.getChoices() != null && !openAIResponse.getChoices().isEmpty()) {
                return openAIResponse.getChoices().get(0).getMessage().getContent();
            }
        }
        return "Ошибка: пустой или некорректный ответ от OpenAI.";
    }
}
