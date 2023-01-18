package com.increff.pos.dto;

import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Component
@Service
public class UserDto {

    @Autowired
    private final UserService service;

    public UserDto(UserService service) {
        this.service = service;
    }

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
