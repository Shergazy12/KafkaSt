package com.zstan.kafka.st.Controller;

import com.zstan.kafka.st.Entity.Product;
import com.zstan.kafka.st.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            List<Product> products = productService.getProductsByCategory(categoryId);
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product.getName(), product.getPrice(), product.getCategory().getId());
        return ResponseEntity.ok(createdProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Товар удалён");
    }
}

