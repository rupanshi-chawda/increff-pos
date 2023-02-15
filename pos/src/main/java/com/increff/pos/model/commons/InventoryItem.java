package com.increff.pos.model.commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//todo don't use this in forms and data
    private Integer id;
    private String brand;
    private String category;
    private Integer quantity;

}