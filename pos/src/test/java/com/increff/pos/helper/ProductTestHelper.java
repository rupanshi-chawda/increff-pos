package com.increff.pos.helper;

import com.increff.pos.model.form.ProductForm;

public class ProductTestHelper {

    public static ProductForm createForm(String brand, String category, String barcode, String name, Double mrp){
        ProductForm f = new ProductForm();
        f.setBarcode(barcode);
        f.setBrand(brand);
        f.setCategory(category);
        f.setMrp(mrp);
        f.setName(name);
        return f;
    }

}
