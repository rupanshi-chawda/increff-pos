package com.increff.pos.helper;

import com.increff.pos.model.form.InventoryForm;

public class InventoryTestHelper {

    public static InventoryForm createForm(String barcode, Integer quantity){
        InventoryForm f = new InventoryForm();
        f.setBarcode(barcode);
        f.setQuantity(quantity);
        return f;
    }
}
