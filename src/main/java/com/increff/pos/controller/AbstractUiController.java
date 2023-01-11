package com.increff.pos.controller;

import com.increff.pos.dto.AbstractUiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;

@Controller
public abstract class AbstractUiController {

	@Autowired
	private AbstractUiDto dto;

	protected ModelAndView mav(String page) {
		return dto.mav(page);
	}

}
