package com.increff.pos.helper;

import com.increff.pos.model.data.BrandErrorData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.*;

import java.util.ArrayList;
import java.util.List;


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

    public static void validateFormLists(List<BrandForm> forms) throws ApiException {
        List<BrandErrorData> errorData = new ArrayList<>();
        Integer errorSize = 0;
        for(BrandForm f: forms)
        {
            BrandErrorData brandErrorData = ConvertUtil.convert(f, BrandErrorData.class);
            brandErrorData.setMessage("");
            try
            {
                ValidationUtil.validateForms(f);
                BrandHelper.normalize(f);
            }
            catch (ApiException e) {
                errorSize++;
                brandErrorData.setMessage(e.getMessage());
            }
            errorData.add(brandErrorData);
        }
        if (errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }
    }
}