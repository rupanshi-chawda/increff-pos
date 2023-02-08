package com.increff.pos.helper;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;

public class UserHelper {

    public static void normalize(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
        p.setRole(p.getRole().toLowerCase().trim());
    }

    public static UserData convert(UserPojo p) {
        UserData d = new UserData();
        d.setEmail(p.getEmail());
        d.setRole(p.getRole());
        d.setId(p.getId());
        return d;
    }

    public static UserPojo convert(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setRole("operator");
        p.setPassword(f.getPassword());
        return p;
    }

    public static UserPojo convert(UserForm f, String role) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setRole(role);
        p.setPassword(f.getPassword());
        return p;
    }
}
