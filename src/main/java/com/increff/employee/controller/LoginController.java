package com.increff.employee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.increff.employee.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.form.LoginForm;
import com.increff.employee.util.ApiException;

import io.swagger.annotations.ApiOperation;

@Controller
public class LoginController {

	@Autowired
	private LoginDto dto;
	
	@ApiOperation(value = "Logs in a user")
	@PostMapping(path = "/session/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
		return dto.login(req, f);
	}

	@GetMapping(path = "/session/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		return dto.logout(request, response);
	}

}
