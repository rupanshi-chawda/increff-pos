package com.increff.pos.dto;

import com.increff.pos.helper.UserHelper;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.api.UserApi;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class UserDto {

    @Autowired
    private UserApi api;

    public void add(UserPojo p) throws ApiException {
        UserHelper.normalize(p);
        checkEmail(p.getEmail());
        api.add(p);
    }

    //Checkers
    public void checkEmail(String email) throws ApiException {
        UserPojo existing = getUserByEmail(email);
        if (Objects.nonNull(existing)) {
            throw new ApiException("User with given email already exists");
        }
    }

    public boolean checkEmailExists(String email) throws ApiException {
        UserPojo existing = getUserByEmail(email);
        return Objects.nonNull(existing);
    }

    public UserPojo getUserByEmail(String email) throws ApiException {
        return api.get(email);
    }

    public List<UserPojo> getAll() {
        return api.getAll();
    }

    public void delete(Integer id) {
        api.delete(id);
    }
}
