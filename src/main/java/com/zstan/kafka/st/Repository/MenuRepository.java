package com.zstan.kafka.st.Repository;


import com.zstan.kafka.st.Entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);
}
