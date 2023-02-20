package com.increff.pos.dto;

import com.increff.pos.helper.UserHelper;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.controller.AbstractUiController;
import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;


@Service
public class InitDto extends AbstractUiController {

    @Autowired
    private UserDto dto;

    @Autowired
    private InfoData info;

    @Value("${app.admin_email}")
    private String admin_email;

    public ModelAndView show() {
        info.setMessage("");
        return mav("init.html");
    }

    public ModelAndView init(UserForm form) throws ApiException {
        info.setMessage("");
        if(StringUtil.isEmpty(form.getEmail()) || StringUtil.isEmpty(form.getPassword())) {
            info.setMessage("Email or Password cannot be empty");
            return mav("init.html");
        }
        if (dto.checkEmailExists(form.getEmail())) {
            info.setMessage("You already have an account, please use existing credentials");
            return mav("login.html");
        }
        if(Objects.equals(form.getEmail(), admin_email))
        {
            UserPojo p = UserHelper.convert(form,"supervisor");
            dto.add(p);
            info.setMessage("Signed Up Successfully, you can login now");
            return mav("login.html");
        }
        else
        {
            info.setMessage("Unauthorized access");
            return mav("init.html");
        }
    }

}
