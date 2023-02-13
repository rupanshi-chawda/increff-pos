package com.increff.pos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryDto dto;

    @ApiOperation(value = "Adds inventory of a product")
    @PostMapping(path = "")
    public void addInventory(@RequestBody List<InventoryForm> forms) throws ApiException {
        dto.add(forms);
    }

    @ApiOperation(value = "Gets inventory of a product by barcode")
    @GetMapping(path = "/{barcode}")
    public InventoryData getInventory(@PathVariable String barcode) throws ApiException {
        return dto.get(barcode);
    }

    @ApiOperation(value = "Gets list of all product inventories")
    @GetMapping(path = "")
    public List<InventoryData> getAllInventory() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates inventory of a product by barcode")
    @PutMapping(path = "/{barcode}")
    public void updateInventory(@PathVariable String barcode, @RequestBody InventoryForm f) throws ApiException {
        dto.update(barcode, f);
    }

    @ApiOperation(value = "Exports inventory of a unique brand category")
    @GetMapping(path = "/exportcsv")
    public void exportToCSV(HttpServletResponse response) throws ApiException {
        dto.generateCsv(response);
    }
}
