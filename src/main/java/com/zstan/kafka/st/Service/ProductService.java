package com.zstan.kafka.st.Service;


import com.zstan.kafka.st.Entity.Category;
import com.zstan.kafka.st.Entity.Product;
import com.zstan.kafka.st.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    /**
     * Получить все товары в категории.
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Найти товар по ID.
     */
    public Product getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Товар с ID " + productId + " не найден");
        }
        return product.get();
    }

    /**
     * Создать новый товар.
     */
    public Product createProduct(String name, Double price, Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId); // Используем CategoryService
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        return productRepository.save(product);
    }

    /**
     * Удалить товар.
     */
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }
}
