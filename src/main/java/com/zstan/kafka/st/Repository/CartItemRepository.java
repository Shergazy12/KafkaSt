package com.zstan.kafka.st.Repository;

import com.zstan.kafka.st.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    void deleteByCartId(Long id);
}
