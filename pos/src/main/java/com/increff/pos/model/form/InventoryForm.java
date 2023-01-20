package com.increff.pos.model.form;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class InventoryForm {

    private String barcode;
    private int quantity;

}
