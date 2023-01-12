package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class InventoryPojo {

    @Id
    private int id;

    @NotNull(message = "Quantity cannot be empty")
    private int quantity;

}
