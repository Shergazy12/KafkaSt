package com.zstan.kafka.st.Controller;

import com.zstan.kafka.st.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<String> getCartContent(@PathVariable Long userId) {
        String cartContent = cartService.getCartContent(userId);
        return ResponseEntity.ok(cartContent);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addToCart(@PathVariable Long userId,
                                            @RequestParam Long productId,
                                            @RequestParam int quantity) {
        String response = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Корзина очищена");
    }
}

