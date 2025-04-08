package com.zstan.kafka.st.Controller;

import com.zstan.kafka.st.Request.CartRequest;
import com.zstan.kafka.st.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addToCart(@PathVariable Long userId,
                                            @RequestBody CartRequest request) {
        String response = cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<String> getCartContent(@PathVariable Long userId) {
        String cartContent = cartService.getCartContent(userId);
        return ResponseEntity.ok(cartContent);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Корзина очищена");
    }
}

