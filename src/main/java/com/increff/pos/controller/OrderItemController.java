package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderItemController {

    @Autowired
    private OrderItemDto dto;

    @ApiOperation(value = "Adds an Order Item")
    @PostMapping(path = "/api/cart")
    public void addOrderItem(@RequestBody List<OrderItemForm> form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Gets an Order Item by Id")
    @GetMapping(path = "/api/cart/{id}")
    public OrderItemData getOrderItem(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Order Items")
    @GetMapping(path = "/api/cart")
    public List<OrderItemData> getAllOrderItem() {
        return dto.getAll();
    }

//    @ApiOperation(value = "Updates an OrderItem")
//    @PutMapping(path = "/api/cart/{id}")
//    public void updateOrderItem(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
//        dto.update(id, f);
//    }
}
