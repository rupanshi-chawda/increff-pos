package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class InventoryController {

//    @Autowired
//    private InventoryDto dto;
//
//    @ApiOperation(value = "Adds a Product Inventory")
//    @PostMapping(path = "/api/inventory")
//    public void addInventory(@RequestBody InventoryForm form) throws ApiException {
//        dto.add(form);
//    }
//
//    @ApiOperation(value = "Gets a Product Inventory by Id")
//    @GetMapping(path = "/api/inventory/{id}")
//    public InventoryData getInventory(@PathVariable int id) throws ApiException {
//        return dto.get(id);
//    }
//
//    @ApiOperation(value = "Gets list of all Product Inventories")
//    @GetMapping(path = "/api/inventory")
//    public List<InventoryData> getAllInventory() {
//        return dto.getAll();
//    }
//
//    @ApiOperation(value = "Updates a Product Inventory")
//    @PutMapping(path = "/api/inventory/{id}")
//    public void updateInventory(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
//        dto.update(id, f);
//    }
}
