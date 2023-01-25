package com.increff.pos.controller;

import com.increff.pos.dto.SalesDto;
import com.increff.pos.model.form.SalesForm;
import com.increff.pos.pojo.SalesPojo;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/sales")
public class SalesController {

    @Autowired
    private SalesDto dto;

    @ApiOperation(value = "Gets all the sales done between dates")
    @PostMapping(path = "/filter")
    public List<SalesPojo> getAllBetweenDates(@RequestBody SalesForm salesForm){
        return dto.getAllBetweenDates(salesForm);
    }

    @ApiOperation(value = "Gets all the Sales done")
    @GetMapping(path = "")
    public List<SalesPojo> getAll() throws ApiException {
        return dto.getAll();
    }
}
