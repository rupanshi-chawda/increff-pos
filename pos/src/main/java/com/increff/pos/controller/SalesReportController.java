package com.increff.pos.controller;

import com.increff.pos.api.OrderApi;
import com.increff.pos.dto.SalesReportDto;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/sales-report")
public class SalesReportController
{
    @Autowired
    private SalesReportDto dto;

    @Autowired
    private OrderApi orderApi;

    @ApiOperation(value = "Gets all the orders")
    @GetMapping(path = "")
    public List<SalesReportData> getAll() throws ApiException {
        return dto.getAll();
    }

    @ApiOperation(value = "Gets orders filtered by dates")
    @PostMapping(path = "/filter")
    public List<SalesReportData> getFilteredData(@RequestBody SalesReportForm form) throws ApiException {
        return dto.getFilterAll(form);
    }

    @ApiOperation(value = "Exports sales report to csv")
    @GetMapping(path = "/exportcsv")
    public void exportToCSV(HttpServletResponse response) throws ApiException {
        dto.generateCsv(response);
    }
}