package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class InventoryPojo extends AbstractVersionPojo {

    @Id
    private int id;

    @Column(nullable = false)
    @Min(value = 0)
    private int quantity;

}
