package com.increff.pos.dto;

import com.increff.pos.controller.AbstractUiController;
import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Configuration
@Service
public class InitDto extends AbstractUiController {

    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;

    public ModelAndView show(UserForm form) throws ApiException {
        info.setMessage("");
        return mav("init.html");
    }

    public ModelAndView init(UserForm form) throws ApiException {
        List<UserPojo> list = service.getAll();
        if (list.size() > 0) {
            info.setMessage("Application already initialized. Please use existing credentials");
        } else {
            form.setRole("admin");
            UserPojo p = convert(form);
            service.add(p);
            info.setMessage("Application initialized");
        }
        return mav("init.html");
    }

    //Conversion Methods
    private static UserPojo convert(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setRole(f.getRole());
        p.setPassword(f.getPassword());
        return p;
    }
}
