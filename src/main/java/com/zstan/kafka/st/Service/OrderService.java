package com.zstan.kafka.st.Service;


import com.zstan.kafka.st.Entity.Order;
import com.zstan.kafka.st.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    /**
     * Создать заказ из корзины.
     */
    public String createOrder(Long userId) {
        Long total = cartService.getCartTotal(userId);
        if (total == 0) {
            return "❌ Ваша корзина пуста. Добавьте товары перед оформлением заказа.";
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");
        order.setTotalPrice((double) total);
        orderRepository.save(order);

        cartService.clearCart(userId); // Очистить корзину после создания заказа
        return "✅ Ваш заказ успешно создан на сумму: " + total + " руб.";
    }

    /**
     * Получить заказы пользователя.
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
