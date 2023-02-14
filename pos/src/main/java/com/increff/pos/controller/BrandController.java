package com.increff.pos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.CsvFileGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/brand")
public class BrandController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a brand")
    @PostMapping(path = "")
    public void addBrand(@RequestBody List<BrandForm> forms) throws ApiException{
        dto.add(forms);
    }

    @ApiOperation(value = "Gets a brand by id")
    @GetMapping(path = "/{id}")
    public BrandData getBrand(@PathVariable Integer id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @GetMapping(path = "")
    public List<BrandData> getAllBrand() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a brand by id")
    @PutMapping(path = "/{id}")
    public void updateBrand(@PathVariable Integer id, @RequestBody BrandForm f) throws ApiException {
        dto.update(id, f);
    }

    @ApiOperation(value = "Exports brand table to a csv report")
    @GetMapping(path = "/exportcsv")
    public void exportToCSV(HttpServletResponse response) throws ApiException {
        dto.generateCsv(response);
    }

}