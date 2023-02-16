package com.increff.pos.helper;

import com.increff.pos.model.data.BrandErrorData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;


public class BrandHelper {

    public static BrandData convert(BrandPojo pojo) {
        return ConvertUtil.convert(pojo, BrandData.class);
    }

    public static BrandPojo convert(BrandForm form) {
        return ConvertUtil.convert(form, BrandPojo.class);
    }

    public static void normalize(BrandForm form) {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
    }

}