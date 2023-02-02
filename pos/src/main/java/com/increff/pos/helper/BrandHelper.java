package com.increff.pos.helper;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.StringUtil;


public class BrandHelper {

    public static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        d.setId(p.getId());
        return d;
    }

    public static BrandPojo convert(BrandForm f) {
        BrandPojo p = new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

    public static void normalize(BrandForm f) {
        f.setBrand(StringUtil.toLowerCase(f.getBrand()));
        f.setCategory(StringUtil.toLowerCase(f.getCategory()));
    }

//    public static void validate(BrandForm f) throws ApiException {
//        if(StringUtil.isEmpty(f.getBrand())) {
//            throw new ApiException("Brand cannot be empty");
//        }
//        if(StringUtil.isEmpty(f.getCategory())) {
//            throw new ApiException("Category cannot be empty");
//        }
//    }
    //todo: common validation and @notblank 
}
