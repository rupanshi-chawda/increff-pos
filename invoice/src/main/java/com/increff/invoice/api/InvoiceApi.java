package com.increff.invoice.api;

import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.model.OrderItemData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class InvoiceApi {
    public ResponseEntity<byte[]> generateInvoice(InvoiceForm form) throws IOException {
        List<OrderItemData> items = form.getOrderItemList();
        Double amt = 0.0;
        for(OrderItemData i : items) {
            Double cur = 0.0;
            cur = i.getSellingPrice() * i.getQuantity();
            i.setMultiplied(cur);
            amt+=cur;
        }
        form.setAmount(amt);
        CreateXmlFile createXMLFileJava = new CreateXmlFile();

        createXMLFileJava.createXML(form);

        PdfFromFop pdfFromFOP = new PdfFromFop();

        pdfFromFOP.createPDF();

        return generateResponse(form);
    }

    private ResponseEntity<byte[]> generateResponse(InvoiceForm form) throws IOException {
        String _filename = "./Pdf/invoice_"+ form.getOrderId() +".pdf";
        //todo: add form.get and wrap in if-else

        Path pdfPath = Paths.get("./Pdf/invoice.pdf");

        byte[] contents = Files.readAllBytes(pdfPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "output.pdf";
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
            System.out.println(o.getMultiplied());
        }
    }

}