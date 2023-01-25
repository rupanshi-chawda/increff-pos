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
@RequestMapping(path = "/api/salesreport")
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

    @ApiOperation(value = "Gets Filtered orders")
    @PostMapping(path = "/filter")
    public List<SalesReportData> getFilteredData(@RequestBody SalesReportForm form) throws ApiException {
        String startDate = form.getStartDate() + " 00:00:00";
        String endDate = form.getEndDate() + " 23:59:59";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime sDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime eDate = LocalDateTime.parse(endDate, formatter);

        ZonedDateTime start = sDate.atZone(ZoneId.systemDefault());
        ZonedDateTime end = eDate.atZone(ZoneId.systemDefault());;

        List<OrderPojo> list = orderApi.getOrderByDateFilter(start, end);
        return dto.getFilterAll(list, form.getBrand(), form.getCategory());
    }

    @ApiOperation(value = "Export Product Report to CSV")
    @GetMapping(path = "/exportcsv")
    public void exportToCSV(HttpServletResponse response) throws IOException, ApiException {
        dto.generateCsv(response);
    }
}