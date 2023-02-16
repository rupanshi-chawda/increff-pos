package com.increff.pos.helper;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ConvertUtil;

public class UserHelper {

    public static void normalize(UserPojo pojo) {
        pojo.setEmail(pojo.getEmail().toLowerCase().trim());
        pojo.setRole(pojo.getRole().toLowerCase().trim());
    }

    public static UserData convert(UserPojo pojo) {
        return  ConvertUtil.convert(pojo, UserData.class);
    }

    public static UserPojo convert(UserForm form) {
        UserPojo pojo = ConvertUtil.convert(form, UserPojo.class);
        pojo.setRole("operator");
        return pojo;
    }

    public static UserPojo convert(UserForm form, String role) {
        UserPojo pojo = ConvertUtil.convert(form, UserPojo.class);
        pojo.setRole(role);
        return pojo;
    }
}
