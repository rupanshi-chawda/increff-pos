package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderController {
    
    @Autowired
    private OrderDto dto;

    @ApiOperation(value = "Adds an Order")
    @PostMapping(path = "/api/order")
    public void addOrder(@RequestBody OrderForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Gets an Order by Id")
    @GetMapping(path = "/api/order/{id}")
    public OrderData getOrder(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Orders")
    @GetMapping(path = "/api/order")
    public List<OrderData> getAllOrder() {
        return dto.getAll();
    }

}
