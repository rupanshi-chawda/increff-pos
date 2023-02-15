package com.increff.invoice.api;

import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.util.ApiException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class InvoiceApi {
    public String generateInvoice(InvoiceForm form) throws ApiException {

            XmlGeneration xmlGeneration = new XmlGeneration();
            xmlGeneration.createXML(form);
            String base64 = xmlGeneration.createXML(form);
            PdfFromFop pdfFromFOP = new PdfFromFop();

            return pdfFromFOP.createPDF(base64);
    }
}