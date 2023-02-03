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

    @ApiOperation(value = "Gets all the Sales data")
    @GetMapping(path = "")
    public List<SalesPojo> getAll() throws ApiException {
        return dto.getAll();
    }

    @ApiOperation(value = "get all sales date between 2 dates")
    @PostMapping(path = "/filter")
    public List<SalesPojo> getAllByDate(@RequestBody SalesForm form){
        return dto.getAllBetweenDates(form);
    }

    @ApiOperation(value = "Runs the scheduler")
    @GetMapping(path = "/scheduler")
    public void runScheduler() throws ApiException {
        dto.createReport();
    }
}
//todo: add check that end date must not be lesser than start date