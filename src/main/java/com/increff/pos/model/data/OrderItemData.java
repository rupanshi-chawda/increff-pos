package com.increff.pos.model.data;

import com.increff.pos.model.form.OrderItemForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {
    private int id;
    private int orderId;
    private int productId;
}
