package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter//TODO add indexes (unique/non-unique) and read about this db optimisation mysql and use box data types
public class OrderItemPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    @Min(value = 0)
    private int quantity;

    @Column(nullable = false)
    @Min(value = 1)
    private Double sellingPrice;
}
