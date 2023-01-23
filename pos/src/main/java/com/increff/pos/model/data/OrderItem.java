package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    private int orderItemId;
    private Double sellingPrice;
    private String productName;
    private int quantity;
}