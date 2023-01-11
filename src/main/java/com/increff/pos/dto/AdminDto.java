package com.increff.pos.dto;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Service
public class AdminDto {

    @Autowired
    private UserService service;

    public void add(UserForm form) throws ApiException {
        UserPojo p = convert(form);
        service.add(p);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public List<UserData> getAll(){
        List<UserPojo> list = service.getAll();
        List<UserData> list2 = new ArrayList<UserData>();
        for (UserPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    //Conversion Methods

    private static UserData convert(UserPojo p) {
        UserData d = new UserData();
        d.setEmail(p.getEmail());
        d.setRole(p.getRole());
        d.setId(p.getId());
        return d;
    }

    private static UserPojo convert(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setRole(f.getRole());
        p.setPassword(f.getPassword());
        return p;
    }
}
