package com.increff.pos.helper;

import com.increff.pos.model.form.OrderItemForm;

public class OrderTestHelper {

    public static OrderItemForm createForm(String barcode, int quantity, Double sellingPrice){
        OrderItemForm f = new OrderItemForm();
        f.setBarcode(barcode);
        f.setQuantity(quantity);
        f.setSellingPrice(sellingPrice);
        return f;
    }

}
