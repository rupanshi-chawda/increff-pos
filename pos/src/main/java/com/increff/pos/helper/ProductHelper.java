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

    public static ProductData convert(ProductPojo pojo, BrandPojo brandPojo) {
        ProductData data = ConvertUtil.convert(pojo, ProductData.class);
        data.setBrand(brandPojo.getBrand());
        data.setCategory(brandPojo.getCategory());
        return data;
    }

    public static ProductPojo convert(ProductForm form) {
        return ConvertUtil.convert(form, ProductPojo.class);
    }

    public static ProductPojo convert(ProductUpdateForm updateForm) {
        return ConvertUtil.convert(updateForm, ProductPojo.class);
    }

    public static void normalize(ProductForm form) {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
        form.setName(StringUtil.toLowerCase(form.getName()));
    }

    public static void normalize(ProductUpdateForm form) {
        form.setName(StringUtil.toLowerCase(form.getName()));
    }

}
