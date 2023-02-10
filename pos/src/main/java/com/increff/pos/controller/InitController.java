package com.increff.pos.controller;

import com.increff.pos.dto.InitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.form.UserForm;
import com.increff.pos.util.ApiException;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(path = "/site/init")
public class InitController {

	@Autowired
	private InitDto dto;

	@ApiOperation(value = "Initializes application")
	@GetMapping(path = "")
	public ModelAndView showPage() throws ApiException {
		return dto.show();
	}

	@ApiOperation(value = "Initializes application")
	@PostMapping(path = "")
	public ModelAndView initSite(UserForm form) throws ApiException {
		return dto.init(form);
	}

}
