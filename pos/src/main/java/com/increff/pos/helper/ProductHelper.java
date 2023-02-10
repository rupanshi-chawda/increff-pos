package com.increff.pos.helper;

import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductUpdateForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class ProductHelper {

    public static ProductData convert(ProductPojo p, BrandPojo b) {
        ProductData d = ConvertUtil.convert(p, ProductData.class);
        d.setBrand(b.getBrand());
        d.setCategory(b.getCategory());
        return d;
    }

    public static ProductPojo convert(ProductForm f) {
        return ConvertUtil.convert(f, ProductPojo.class);
    }

    public static ProductPojo convert(ProductUpdateForm f) {
        return ConvertUtil.convert(f, ProductPojo.class);
    }

    public static void normalize(ProductForm f) {
        f.setBarcode(StringUtil.toLowerCase(f.getBarcode()));
        f.setBrand(StringUtil.toLowerCase(f.getBrand()));
        f.setCategory(StringUtil.toLowerCase(f.getCategory()));
        f.setName(StringUtil.toLowerCase(f.getName()));
    }

    public static void normalize(ProductUpdateForm f) {
        f.setName(StringUtil.toLowerCase(f.getName()));
    }

}
