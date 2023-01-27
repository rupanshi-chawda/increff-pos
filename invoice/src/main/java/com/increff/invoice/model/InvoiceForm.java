package com.increff.invoice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceForm {
    private Integer orderId;
    private String placeDate;
    private List<OrderItemData> orderItemList;
    private Double amount;
}