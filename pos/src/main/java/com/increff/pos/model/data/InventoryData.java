package com.increff.pos.model.data;

import com.increff.pos.model.form.InventoryForm;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class InventoryData extends InventoryForm {
    private Integer id;
    private Double mrp;
}
