package com.increff.pos.helper;

import com.increff.pos.model.form.UserForm;

public class UserTestHelper {

    public static UserForm createForm(String email, String password, String role) {
        UserForm userForm = new UserForm();
        userForm.setEmail(email);
        userForm.setPassword(password);
        userForm.setRole(role);
        return userForm;
    }
}
