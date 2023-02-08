package com.increff.invoice.controller;

import com.increff.invoice.dto.InvoiceDto;
import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.util.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api
@RestController
public class InvoiceController {

    @Autowired
    private InvoiceDto dto;

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path = "/api/invoice", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getPDF(@RequestBody InvoiceForm form) throws ApiException {

        return dto.generateInvoice(form);
    }


}