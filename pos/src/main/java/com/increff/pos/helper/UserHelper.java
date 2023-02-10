package com.increff.pos.helper;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ConvertUtil;

public class UserHelper {

    public static void normalize(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
        p.setRole(p.getRole().toLowerCase().trim());
    }

    public static UserData convert(UserPojo p) {
        return  ConvertUtil.convert(p, UserData.class);
    }

    public static UserPojo convert(UserForm f) {
        UserPojo p = ConvertUtil.convert(f, UserPojo.class);
        p.setRole("operator");
        return p;
    }

    public static UserPojo convert(UserForm f, String role) {
        UserPojo p = ConvertUtil.convert(f, UserPojo.class);
        p.setRole(role);
        return p;
    }
}
