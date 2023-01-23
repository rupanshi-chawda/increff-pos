package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class OrderItemPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Order ID cannot be Empty")
    private int orderId;

    @NotNull(message = "Product ID cannot be Empty")
    private int productId;

    @NotNull(message = "Quantity cannot be Empty")
    @Min(value = 0)
    private int quantity;

    @NotNull(message = "Selling Price cannot be Empty")
    @Min(value = 1)
    private Double sellingPrice;
}
