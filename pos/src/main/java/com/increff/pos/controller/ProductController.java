package com.increff.pos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.ProductUpdateForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/product")
public class ProductController {

    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Adds a Product")
    @PostMapping(path = "")
    public void addProduct(@RequestBody List<ProductForm> forms) throws ApiException, JsonProcessingException {
        dto.add(forms);
    }

    @ApiOperation(value = "Gets a Product by Id")
    @GetMapping(path = "/{id}")
    public ProductData getProduct(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Products")
    @GetMapping(path = "")
    public List<ProductData> getAllProduct() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Product")
    @PutMapping(path = "/{id}")
    public void updateProduct(@PathVariable int id, @RequestBody ProductUpdateForm f) throws ApiException {
        dto.update(id, f);
    }

}
