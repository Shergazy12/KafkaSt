package com.zstan.kafka.st.Controller;

import com.zstan.kafka.st.Entity.Order;
import com.zstan.kafka.st.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<String> createOrder(@PathVariable Long userId) {
        String response = orderService.createOrder(userId);
        return ResponseEntity.ok(response);
    }
}

