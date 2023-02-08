package com.increff.invoice.api;

import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.model.OrderItemData;
import com.increff.invoice.util.ApiException;
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
import java.util.Base64;
import java.util.List;

@Service
public class InvoiceApi {
    public String generateInvoice(InvoiceForm form) throws ApiException {
        String _filename = "./Pdf/invoice_"+ form.getOrderId() +".pdf";
        File f = new File(_filename);
        if(!f.exists()){
            CreateXmlFile createXMLFileJava = new CreateXmlFile();
            createXMLFileJava.createXML(form);
            String base64 = createXMLFileJava.createXML(form);
            PdfFromFop pdfFromFOP = new PdfFromFop();

            return pdfFromFOP.createPDF(form, base64);
        }
        else {
            Path pdfPath = Paths.get("./Pdf/invoice_"+ form.getOrderId() +".pdf");
            byte[] pdf = new byte[0];
            try {
                pdf = Files.readAllBytes(pdfPath);
            } catch (IOException e) {
                throw new ApiException(e.getMessage());
            }
            String base64 = Base64.getEncoder().encodeToString(pdf);
            return base64;
        }
    }

//    private ResponseEntity<byte[]> generateResponse(InvoiceForm form) throws ApiException {
//        String _filename = "./Pdf/invoice_"+ form.getOrderId() +".pdf";
//
//        Path pdfPath = Paths.get("./Pdf/invoice_"+ form.getOrderId() +".pdf");
//
//        byte[] contents;
//        try {
//            contents = Files.readAllBytes(pdfPath);
//        } catch (IOException e) {
//            throw new ApiException(e.getMessage());
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        // set the actual filename of your pdf
//        String filename = "output\"+ form.getOrderId() +\".pdf";
//        headers.setContentDispositionFormData(filename, filename);
//        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
//        return response;
//    }

}