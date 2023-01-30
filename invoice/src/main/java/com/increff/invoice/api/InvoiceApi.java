package com.increff.invoice.api;

import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.model.OrderItemData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class InvoiceApi {
    public ResponseEntity<byte[]> generateInvoice(InvoiceForm form) throws IOException {
        String _filename = "./Pdf/invoice_"+ form.getOrderId() +".pdf";
        File f = new File(_filename);
        if(f.exists()){
            System.out.println("inside exists");
        }
        else {
            CreateXmlFile createXMLFileJava = new CreateXmlFile();
            createXMLFileJava.createXML(form);
            System.out.println("inside NOT exists");
        }

        return generateResponse(form);
    }

    private ResponseEntity<byte[]> generateResponse(InvoiceForm form) throws IOException {
        String _filename = "./Pdf/invoice_"+ form.getOrderId() +".pdf";

        Path pdfPath = Paths.get("./Pdf/invoice_"+ form.getOrderId() +".pdf");

        byte[] contents = Files.readAllBytes(pdfPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "output\"+ form.getOrderId() +\".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    private void print(InvoiceForm form)
    {
        System.out.println(form.getOrderId());
        System.out.println(form.getPlaceDate());

        List<OrderItemData> orderItemList = form.getOrderItemList();

        System.out.println(orderItemList.size());

        for(OrderItemData o :orderItemList)
        {
            System.out.println(o.getProductName());
            System.out.println(o.getOrderItemId());
            System.out.println(o.getQuantity());
            System.out.println(o.getSellingPrice());
        }
    }

}