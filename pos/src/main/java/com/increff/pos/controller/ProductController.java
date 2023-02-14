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

    @ApiOperation(value = "Adds a product")
    @PostMapping(path = "")
    public void addProduct(@RequestBody List<ProductForm> forms) throws ApiException {
        dto.add(forms);
    }

    @ApiOperation(value = "Gets a product by id")
    @GetMapping(path = "/{id}")
    public ProductData getProduct(@PathVariable Integer id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all products")
    @GetMapping(path = "")
    public List<ProductData> getAllProduct() throws ApiException {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a product by id")
    @PutMapping(path = "/{id}")
    public void updateProduct(@PathVariable Integer id, @RequestBody ProductUpdateForm f) throws ApiException {
        dto.update(id, f);
    }

}
