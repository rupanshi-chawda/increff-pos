package com.increff.employee.dto;

import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.UserService;
import com.increff.employee.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class UserDto {

    @Autowired
    private UserService service;

    public void add(UserPojo p) throws ApiException {
        normalize(p);
        checkEmail(p);
        service.add(p);
    }


    //Checkers and normalizers
    public static void normalize(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
        p.setRole(p.getRole().toLowerCase().trim());
    }

    public void checkEmail(UserPojo p) throws ApiException {
        UserPojo existing = service.getUserEmail(p);
        if (Objects.isNull(existing)) {
            throw new ApiException("User with given email already exists");
        }
    }
}
