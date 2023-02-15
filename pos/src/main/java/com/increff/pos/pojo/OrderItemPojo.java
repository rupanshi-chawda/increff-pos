package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class OrderItemPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//todo y do we need generation type table and its impact on multi threading
    private Integer id;

    @Column(nullable = false)
    private Integer orderId;

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    @Min(value = 0)
    private Integer quantity;

    @Column(nullable = false)
    @Min(value = 1)
    private Double sellingPrice;
}
