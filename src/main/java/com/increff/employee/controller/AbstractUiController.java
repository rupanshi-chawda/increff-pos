package com.increff.employee.controller;

import com.increff.employee.dto.AbstractUiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.model.data.InfoData;
import com.increff.employee.util.SecurityUtil;
import com.increff.employee.util.UserPrincipal;

@Controller
public abstract class AbstractUiController {

	@Autowired
	private AbstractUiDto dto;

	protected ModelAndView mav(String page) {
		return dto.mav(page);
	}

}
