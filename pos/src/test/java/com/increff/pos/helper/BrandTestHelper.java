package com.increff.pos.helper;

import com.increff.pos.model.form.BrandForm;

public class BrandTestHelper {

    public static BrandForm createForm(String brand, String category){
        BrandForm f = new BrandForm();
        f.setBrand(brand);
        f.setCategory(category);
        return f;
    }

}

