package com.increff.employee.dto;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class UserDto {

    @Autowired
    private UserDao dao;

    public static void normalize(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
        p.setRole(p.getRole().toLowerCase().trim());
    }

    public void getCheck(UserPojo p) throws ApiException {
        UserPojo existing = dao.select(p.getEmail());
        if (Objects.isNull(existing)) {
            throw new ApiException("User with given email already exists");
        }
    }
}
