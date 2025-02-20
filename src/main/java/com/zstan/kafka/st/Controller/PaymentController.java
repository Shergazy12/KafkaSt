package com.zstan.kafka.st.Controller;

import com.zstan.kafka.st.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{userId}/create-intent")
    public ResponseEntity<String> createPaymentIntent(@PathVariable Long userId,
                                                      @RequestParam Long amount,
                                                      @RequestParam String currency) {
        String paymentLink = paymentService.createPaymentIntent(amount, currency);
        if (paymentLink != null) {
            return ResponseEntity.ok("Ссылка на оплату: " + paymentLink);
        }
        return ResponseEntity.badRequest().body("Не удалось создать платёжное намерение");
    }
}

