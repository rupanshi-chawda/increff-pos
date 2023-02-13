package com.increff.pos.controller;

import com.increff.pos.util.ApiException;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
//import com.increff.pos.model.form.OrderForm;
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

    @ApiOperation(value = "Gets list of all orders")
    @GetMapping(path = "")
    public List<OrderData> getAllOrder() {
        return dto.getAllOrder();
    }

    @ApiOperation(value = "Adds an order item")
    @PostMapping(path = "/cart")
    public void addOrderItem(@RequestBody List<OrderItemForm> form) throws ApiException {
        dto.addItem(form);
    }

    @ApiOperation(value = "Gets list of order items of an order by orderid")
    @GetMapping(path = "/cartitems/{id}")
    public List<OrderItemData> getOrderItemById(@PathVariable int id) throws ApiException {
        return dto.getByOrderId(id);
    }

    @ApiOperation(value = "Downloads order invoice by id")
    @GetMapping(path = "/invoice/{id}", produces =  "application/pdf")
    public ResponseEntity<byte[]> getPDF(@PathVariable int id) throws ApiException{
        return dto.getPDF(id);
    }
// todo: move invoice saving to pos and add filepath column to orderpojo
}
