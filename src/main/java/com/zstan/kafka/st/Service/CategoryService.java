package com.zstan.kafka.st.Service;


import com.zstan.kafka.st.Entity.Category;
import com.zstan.kafka.st.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Получить все категории.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Найти категорию по ID.
     */
    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Категория с ID " + id + " не найдена");
        }
        return category.get();
    }

    /**
     * Создать новую категорию.
     */
    public Category createCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Категория с именем '" + name + "' уже существует");
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    /**
     * Удалить категорию.
     */
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id); // Используем ранее созданный метод
        categoryRepository.delete(category);
    }
}