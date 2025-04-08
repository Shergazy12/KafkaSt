package com.zstan.kafka.st.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartRequest {

    private Long productId;
    private int quantity;

    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}


