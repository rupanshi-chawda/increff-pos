package com.increff.employee.controller;

import com.increff.employee.dto.ProductDto;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Adds a Product")
    @PostMapping(path = "/api/product")
    public void addProduct(@RequestBody ProductForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Gets a Product by Id")
    @GetMapping(path = "/api/product/{id}")
    public ProductData getProduct(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Products")
    @GetMapping(path = "/api/product")
    public List<ProductData> getAllProduct() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Product")
    @PutMapping(path = "/api/product/{id}")
    public void updateProduct(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
        dto.update(id, f);
    }

}
