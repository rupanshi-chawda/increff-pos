package com.increff.pos.dto;

import com.increff.pos.helper.UserHelper;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.StringUtil;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminDto {

    @Autowired
    private UserDto dto;

    public void add(UserForm form) throws ApiException {
        if(StringUtil.isEmpty(form.getEmail())) {
            throw new ApiException("Email cannot be empty");
        }
        if(StringUtil.isEmpty(form.getPassword())) {
            throw new ApiException("Password cannot be empty");
        }
        if(form.getPassword().length() < 6) {
            throw new ApiException("Password must be at least 6 characters long");
        }
        UserPojo p = UserHelper.convert(form);
        dto.add(p);
    }

    public void delete(Integer id) {
        dto.delete(id);
    }

    public List<UserData> getAll(){
        return dto.getAll().stream().map(UserHelper::convert).collect(Collectors.toList());
    }

}
