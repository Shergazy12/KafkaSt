package com.zstan.kafka.st.Service;


import com.zstan.kafka.st.Entity.MenuItem;
import com.zstan.kafka.st.Repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<MenuItem> getByRestaurantId(Long restaurantId){
        return menuRepository.findByRestaurantId(restaurantId);
    }

    public List<MenuItem> getMenuByRestaurantId(Long restaurantId) {
        return null;
    }
}
