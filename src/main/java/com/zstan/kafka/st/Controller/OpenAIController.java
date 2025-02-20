package com.zstan.kafka.st.Controller;

import com.zstan.kafka.st.OpenAI.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/ask")
    public ResponseEntity<String> askGPT(@RequestParam String question) {
        String response = openAIService.getGPTResponse(question);
        return ResponseEntity.ok(response);
    }
}

