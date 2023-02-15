package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {

	@Email
	@NotBlank
	@Size(min = 1, max = 25,message = " must be between 1 and 25 characters long ")
	private String email;

	@NotBlank
	@Size(min = 6, message = "must be atleast 6 character!")
	private String password;

	@NotBlank
	private String role;

}
