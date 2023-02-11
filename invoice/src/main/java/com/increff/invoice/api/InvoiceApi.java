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
        String _filename = "./Pdf/invoice_" + form.getOrderId() + ".pdf";
        File f = new File(_filename);
        if (!f.exists()) {
            XmlGeneration xmlGeneration = new XmlGeneration();
            xmlGeneration.createXML(form);
            String base64 = xmlGeneration.createXML(form);
            PdfFromFop pdfFromFOP = new PdfFromFop();

            return pdfFromFOP.createPDF(form, base64);
        } else {
            Path pdfPath = Paths.get("./Pdf/invoice_" + form.getOrderId() + ".pdf");
            byte[] pdf;
            try {
                pdf = Files.readAllBytes(pdfPath);
            } catch (IOException e) {
                throw new ApiException(e.getMessage());
            }
            String base64 = Base64.getEncoder().encodeToString(pdf);
            return base64;
        }
    }
}