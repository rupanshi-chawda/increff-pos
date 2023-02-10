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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;

public class LoginDtoTest extends AbstractUnitTest {

    @Autowired
    private UserDto userDto;

    @Autowired
    private LoginDto dto;

    @Autowired
    private AdminDto adminDto;

    @Autowired
    private InfoData info;

    @Test
    public void loginTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("test@mail.com", "1234abcd", "supervisor");
        adminDto.add(userForm);

        LoginForm form = new LoginForm();
        form.setEmail("test@mail.com");
        form.setPassword("1234abcd");

        MockHttpServletRequest req = new MockHttpServletRequest();
        ModelAndView mav = dto.login(req, form);

        String expected = "redirect:/ui/home";
        assertEquals(expected, mav.getViewName());
    }

    @Test
    public void invalidPasswordTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("test@mail.com", "1234abcd", "supervisor");
        adminDto.add(userForm);

        LoginForm form = new LoginForm();
        form.setEmail("test@mail.com");
        form.setPassword("asdfg");

        MockHttpServletRequest req = new MockHttpServletRequest();
        ModelAndView mav = dto.login(req, form);

        String expected = "redirect:/site/login";
        String expectedInfo = "Invalid password";
        assertEquals(expected, mav.getViewName());
        assertEquals(expectedInfo, info.getMessage());
    }

    @Test
    public void invalidEmailTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("test@mail.com", "1234abcd", "supervisor");
        adminDto.add(userForm);

        LoginForm form = new LoginForm();
        form.setEmail("fake@mail.com");
        form.setPassword("1234abcd");

        MockHttpServletRequest req = new MockHttpServletRequest();
        ModelAndView mav = dto.login(req, form);

        String expected = "redirect:/site/login";
        String expectedInfo = "Invalid username";
        assertEquals(expected, mav.getViewName());
        assertEquals(expectedInfo, info.getMessage());
    }

    @Test
    public void logoutTest() throws ApiException {

        MockHttpServletRequest req = new MockHttpServletRequest();
        ModelAndView mav = dto.logout(req);

        String expected = "redirect:/site/logout";
        assertEquals(expected, mav.getViewName());
    }
}
