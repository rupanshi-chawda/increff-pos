package com.increff.employee.controller;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BrandController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a Brand")
    @PostMapping(path = "/api/brand")
    public void addBrand(@RequestBody BrandForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Gets a Brand by Id")
    @GetMapping(path = "/api/brand/{id}")
    public BrandData getBrand(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Brands")
    @GetMapping(path = "/api/brand")
    public List<BrandData> getAllBrand() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Brand")
    @PutMapping(path = "/api/brand/{id}")
    public void updateBrand(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
        dto.update(id, f);
    }

}
