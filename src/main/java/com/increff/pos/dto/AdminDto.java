package com.increff.pos.dto;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class AdminDto {

    @Autowired
    private final UserService service;

    public AdminDto(UserService service) {
        this.service = service;
    }

    public void add(UserForm form) throws ApiException {
        if(StringUtil.isEmpty(form.getEmail())) {
            throw new ApiException("Email cannot be empty");
        }
        if(StringUtil.isEmpty(form.getPassword())) {
            throw new ApiException("Password cannot be empty");
        }
        if(StringUtil.isEmpty(form.getRole())) {
            throw new ApiException("Role cannot be empty");
        }
        UserPojo p = convert(form);
        service.add(p);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public List<UserData> getAll(){
        return service.getAll().stream().map(p -> convert(p)).collect(Collectors.toList());
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
