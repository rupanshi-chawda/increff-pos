package com.increff.employee.controller;

import com.increff.employee.service.ProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductService service;

}
