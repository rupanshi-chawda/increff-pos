package com.increff.pos.dto;

import com.increff.pos.pojo.UserPojo;
import com.increff.pos.api.UserApi;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Component
@Service
public class UserDto {

    @Autowired
    private UserApi api;

    public void add(UserPojo p) throws ApiException {
        normalize(p);
        checkEmail(p);
        api.add(p);
    }

    //Checkers and normalizers
    public static void normalize(UserPojo p) {
        p.setEmail(p.getEmail().toLowerCase().trim());
        p.setRole(p.getRole().toLowerCase().trim());
    }

    public void checkEmail(UserPojo p) throws ApiException {
        UserPojo existing = api.getUserEmail(p);
        System.out.println("inside check email");
        System.out.println(existing);
        if (!Objects.isNull(existing)) {
            throw new ApiException("User with given email already exists");
        }
    }

    public UserPojo getEmail(String email) throws ApiException {
        return api.get(email);
    }

    public List<UserPojo> getAll() {
        return api.getAll();
    }

    public void delete(int id) {
        api.delete(id);
    }
}
