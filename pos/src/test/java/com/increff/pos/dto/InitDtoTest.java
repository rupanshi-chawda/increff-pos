package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.UserTestHelper;
import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;

public class InitDtoTest extends AbstractUnitTest {

    @Autowired
    private AdminDto adminDto;

    @Autowired
    private InitDto dto;

    @Autowired
    private InfoData info;

    @Test
    public void initTest() {
        ModelAndView mav = dto.show();

        String expected = "init.html";
        assertEquals(expected, mav.getViewName());
    }

    @Test
    public void signUpTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("arun@gmail.com", "arun2023", "");

        ModelAndView mav = dto.init(userForm);

        String expectedInfo = "Signed Up Successfully, you can login now";
        assertEquals(expectedInfo, info.getMessage());
    }

    @Test
    public void signUpExistsTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("test@mail.com", "1234abcd", "");
        adminDto.add(userForm);

        ModelAndView mav = dto.init(userForm);

        String expectedInfo = "You already have an account, please use existing credentials";
        assertEquals(expectedInfo, info.getMessage());
    }

    @Test()
    public void signUpEmptyTest() throws ApiException {

        UserForm userForm = UserTestHelper.createForm(" ", " ", "");
        ModelAndView mav = dto.init(userForm);

        String expectedInfo = "Email or Password cannot be empty";
        assertEquals(expectedInfo, info.getMessage());
    }

    @Test
    public void signUpIllegalTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail", "1234", "");

        ModelAndView mav = dto.init(userForm);

        String expectedInfo = "Unauthorized access";
        assertEquals(expectedInfo, info.getMessage());
    }

}
