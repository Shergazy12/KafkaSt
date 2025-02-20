package com.zstan.kafka.st.Repository;

import com.zstan.kafka.st.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

}
