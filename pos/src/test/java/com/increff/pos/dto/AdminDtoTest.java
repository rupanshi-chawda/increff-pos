package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.UserTestHelper;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
//todo rewrite tests
public class AdminDtoTest extends AbstractUnitTest {

    @Autowired
    private AdminDto dto;

    @Autowired
    private UserDto userDto;

    @Test
    public void userAddTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail.com", "1234abcd", "supervisor");
        dto.add(userForm);

        List<UserData> userDataList = dto.getAll();
        assertEquals(1, userDataList.size());
    }

    @Test(expected = ApiException.class)
    public void addEmptyUserTest() throws ApiException {
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
        dto.add(userForm);

        List<UserData> userDataList = dto.getAll();
        assertEquals(1, userDataList.size());

        dto.delete(userDataList.get(0).getId());
        List<UserData> userDataList2 = dto.getAll();
        assertEquals(0, userDataList2.size());

    }

    @Test
    public void userGetTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail.com", "1234abcd", "supervisor");
        dto.add(userForm);

        List<UserData> userDataList = dto.getAll();
        assertEquals(1, userDataList.size());

        assertTrue(userDto.checkEmailExists("fake@mail.com"));
    }

    @Test
    public void checkIllegalEmailTest() throws ApiException {
        UserForm userForm = UserTestHelper.createForm("fake@mail.com", "1234abcd", "supervisor");
        dto.add(userForm);

        assertFalse(userDto.checkEmailExists("user@fake.com"));
    }

}
