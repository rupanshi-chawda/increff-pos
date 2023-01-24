package com.increff.pos.controller;

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

    @ApiOperation(value = "Adds a Brand")
    @PostMapping(path = "")
    public void addBrand(@RequestBody BrandForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Gets a Brand by Id")
    @GetMapping(path = "/{id}")
    public BrandData getBrand(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all Brands")
    @GetMapping(path = "")
    public List<BrandData> getAllBrand() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a Brand")
    @PutMapping(path = "/{id}")
    public void updateBrand(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
        dto.update(id, f);
    }

    @ApiOperation(value = "Export Brand Report to CSV")
    @GetMapping(path = "/exportcsv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        dto.generateCsv(response);
    }

}
