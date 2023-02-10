package com.increff.pos.helper;

import com.increff.pos.model.data.BrandErrorData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;


public class BrandHelper {

    public static BrandData convert(BrandPojo p) {
        return ConvertUtil.convert(p, BrandData.class);
    }

    public static BrandPojo convert(BrandForm f) {
        return ConvertUtil.convert(f, BrandPojo.class);
    }

    public static void normalize(BrandForm f) {
        f.setBrand(StringUtil.toLowerCase(f.getBrand()));
        f.setCategory(StringUtil.toLowerCase(f.getCategory()));
    }

}
// todo: generalise convert functions from convertutil