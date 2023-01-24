package com.increff.pos.controller;

import com.increff.pos.util.ApiException;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/order")
public class OrderController {
    
    @Autowired
    private OrderDto dto;

    @ApiOperation(value = "Adds an Order")
    @PostMapping(path = "")
    public void addOrder(@RequestBody OrderForm form) throws ApiException {
        dto.addOrder(form);
    }

    @ApiOperation(value = "Gets an Order by Id")
    @GetMapping(path = "/{id}")
    public OrderData getOrder(@PathVariable int id) throws ApiException {
        return dto.getOrder(id);
    }

    @ApiOperation(value = "Gets list of all Orders")
    @GetMapping(path = "")
    public List<OrderData> getAllOrder() {
        return dto.getAllOrder();
    }

    @ApiOperation(value = "Adds an Order Item")
    @PostMapping(path = "/cart")
    public void addOrderItem(@RequestBody List<OrderItemForm> form) throws ApiException {
        dto.addItem(form);
    }

    @ApiOperation(value = "Gets an Order Item by Id")
    @GetMapping(path = "/cart/{id}")
    public OrderItemData getOrderItem(@PathVariable int id) throws ApiException {
        return dto.getItem(id);
    }

    @ApiOperation(value = "Gets list of all Order Items")
    @GetMapping(path = "/cart")
    public List<OrderItemData> getAllOrderItem() {
        return dto.getAllItem();
    }

    @ApiOperation(value = "Gets list of Order Items in an Order by Id")
    @GetMapping(path = "/cartitems/{id}")
    public List<OrderItemData> getOrderItemById(@PathVariable int id) throws ApiException {
        return dto.getByOrderId(id);
    }

    @ApiOperation(value = "Download Invoice")
    @GetMapping(path = "/invoice/{id}", produces =  "application/pdf")
    public ResponseEntity<byte[]> getPDF(@PathVariable int id) throws Exception{
        return dto.getPDF(id);
    }


}
