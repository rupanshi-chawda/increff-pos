package com.increff.employee.controller;

import com.increff.employee.dto.InitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.form.UserForm;
import com.increff.employee.util.ApiException;

import io.swagger.annotations.ApiOperation;

@Controller
public class InitController {

	@Autowired
	private InitDto dto;

	@ApiOperation(value = "Initializes application")
	@GetMapping(path = "/site/init")
	public ModelAndView showPage(UserForm form) throws ApiException {
		return dto.show(form);
	}

	@ApiOperation(value = "Initializes application")
	@PostMapping(path = "/site/init")
	public ModelAndView initSite(UserForm form) throws ApiException {
		return dto.init(form);
	}

}
