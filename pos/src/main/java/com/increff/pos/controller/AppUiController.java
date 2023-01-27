package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ui")
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}

	@RequestMapping(value = "/brands")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/order")
	public ModelAndView order() {
		return mav("order.html");
	}

	@RequestMapping(value = "/salesreport")
	public ModelAndView salesreport() {
		return mav("salesreport.html");
	}

	@RequestMapping(value = "/sales")
	public ModelAndView sales() {
		return mav("scheduler.html");
	}
}
