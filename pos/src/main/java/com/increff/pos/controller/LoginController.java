package com.increff.pos.controller;

import javax.servlet.http.HttpServletRequest;

import com.increff.pos.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.form.LoginForm;
import com.increff.pos.util.ApiException;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(path = "/session")
public class LoginController {

	@Autowired
	private LoginDto dto;
	
	@ApiOperation(value = "Logs in a user")
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
		return dto.login(req, f);
	}

	@GetMapping(path = "/logout")
	public ModelAndView logout(HttpServletRequest request) {
		return dto.logout(request);
	}

}
