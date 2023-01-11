package com.increff.pos.helper;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.StringUtil;

import java.util.Objects;

public class ProductHelper {

    public static ProductData convert(ProductPojo p) {
        ProductData d = new ProductData();
        d.setBarcode(p.getBarcode());
        d.setId(p.getId());
        d.setName(p.getName());
        d.setMrp(p.getMrp());
        return d;
    }

    public static ProductPojo convert(ProductForm f) {
        ProductPojo p = new ProductPojo();
        p.setName(f.getName());
        p.setBarcode(f.getBarcode());
        p.setMrp(f.getMrp());
        return p;
    }

    public static void normalize(ProductForm f) {
        f.setBarcode(StringUtil.toLowerCase(f.getBarcode()));
        f.setName(StringUtil.toLowerCase(f.getName()));
    }

    public static void checkEmpty(ProductForm f) throws ApiException {
        if(StringUtil.isEmpty(f.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(f.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if(f.getMrp()<1 || Objects.isNull(f.getMrp())) {
            throw new ApiException("MRP cannot be empty or less than one");
        }
    }
}
