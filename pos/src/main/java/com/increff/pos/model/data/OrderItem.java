package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    private Integer orderItemId;
    private Double sellingPrice;
    private String productName;
    private Integer quantity;
}