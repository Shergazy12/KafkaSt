package com.zstan.kafka.st.Service;

import com.zstan.kafka.st.Entity.Cart;
import com.zstan.kafka.st.Entity.CartItem;
import com.zstan.kafka.st.Entity.Product;
import com.zstan.kafka.st.Repository.CartItemRepository;
import com.zstan.kafka.st.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    /**
     * Получить корзину пользователя или создать новую.
     */
    public Cart getOrCreateCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            return cartRepository.save(cart);
        }
        return cart;
    }

    /**
     * Добавить товар в корзину.
     */
    public String addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            Product product = productService.getProductById(productId);
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }

        return "✅ Товар добавлен в корзину!";
    }

    /**
     * Очистить корзину.
     */
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

    /**
     * Подсчитать общую стоимость корзины.
     */
    public Long getCartTotal(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getItems().stream()
                .mapToLong(item -> (long) (item.getProduct().getPrice() * item.getQuantity()))
                .sum();
    }

    public String getCartContent(Long userId) {
        return null;
    }
}