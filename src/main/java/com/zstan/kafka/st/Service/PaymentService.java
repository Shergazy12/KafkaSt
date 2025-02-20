package com.zstan.kafka.st.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    /**
     * Создание платежного намерения через Stripe.
     *
     * @param amount   Сумма платежа в минимальных единицах валюты (копейки/центы, например, 50000 для 500.00 RUB).
     * @param currency Валюта платежа (например, "usd", "eur", "rub").
     * @return Секрет платежного намерения (client_secret) или null в случае ошибки.
     */
    public String createPaymentIntent(Long amount, String currency) {
        // Устанавливаем API-ключ Stripe
        Stripe.apiKey = stripeApiKey;

        // Создаём параметры для платежного намерения
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount) // Сумма в минимальных единицах валюты (копейки, центы)
                .setCurrency(currency) // Валюта
                .addPaymentMethodType(List.of("card").toString())  // Указываем, что используем карты
                .build();

        try {
            // Создаём платежное намерение через Stripe API
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Возвращаем секрет для клиента
            return paymentIntent.getClientSecret();

        } catch (StripeException e) {
            // Возвращаем null в случае ошибки
            return null;
        }
    }
}
