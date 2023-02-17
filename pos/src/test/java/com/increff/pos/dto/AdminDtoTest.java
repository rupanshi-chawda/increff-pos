package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.UserApi;
import com.increff.pos.helper.UserHelper;
import com.increff.pos.helper.UserTestHelper;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class AdminDtoTest extends AbstractUnitTest {

    @Autowired
    private AdminDto dto;

    @Autowired
    private UserApi userApi;
    
    @Test
    public void userAddTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail.com", "1234abcd", "supervisor");
        dto.add(userForm);

        List<UserPojo> userPojoList = userApi.getAll();
        assertEquals(1, userPojoList.size());
    }

    @Test(expected = ApiException.class)
    public void addEmptyUserEmailTest() throws ApiException {
        try {
            UserForm userForm = UserTestHelper.createForm(" ", "12345678", " ");
            dto.add(userForm);
        }
        catch(ApiException e)
        {
            String exception = "Email cannot be empty";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addEmptyUserPasswordTest() throws ApiException {
        try {
            UserForm userForm = UserTestHelper.createForm("fake@gmail.com", "", " ");
            dto.add(userForm);
        }
        catch(ApiException e)
        {
            String exception = "Password cannot be empty";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addExistingUserTest() throws ApiException {
        try {
            UserForm userForm = UserTestHelper.createForm("fake@gmail.com", "12345678", " ");
            dto.add(userForm);
            dto.add(userForm);
        }
        catch(ApiException e)
        {
            String exception = "User with given email already exists";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test
    public void userDeleteTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail.com", "1234abcd", "supervisor");
        userApi.add(UserHelper.convert(userForm));

        List<UserPojo> userPojoList = userApi.getAll();
        assertEquals(1, userPojoList.size());

        dto.delete(userPojoList.get(0).getId());
        List<UserPojo> userPojoList2 = userApi.getAll();
        assertEquals(0, userPojoList2.size());

    }

    @Test
    public void userGetTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail.com", "1234abcd", "supervisor");
        userApi.add(UserHelper.convert(userForm));

        List<UserData> userPojoList = dto.getAll();
        assertEquals(1, userPojoList.size());
        assertNotEquals(null, userApi.get("fake@mail.com"));
    }

    @Test
    public void checkIllegalEmailTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail.com", "1234abcd", "supervisor");
        userApi.add(UserHelper.convert(userForm));
        assertNull(userApi.get("user@fake.com"));
    }

}
