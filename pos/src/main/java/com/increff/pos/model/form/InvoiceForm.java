package com.increff.pos.model.form;

import com.increff.pos.model.data.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceForm {
    private Integer orderId;
    private String placeDate;
    private List<OrderItem> orderItemList;
}