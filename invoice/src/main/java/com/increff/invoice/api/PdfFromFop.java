package com.increff.invoice.api;

import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.util.ApiException;
import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class PdfFromFop {
    public String createPDF(InvoiceForm form, String xmlEncodedString) throws ApiException {
        try {

            File xsltfile = new File("C:\\Users\\KIIT\\Downloads\\increff-pos\\invoice\\src\\main\\resources\\xsl\\invoice.xsl");
            File pdfDir = new File("./Pdf");

            //Creating the XML file here
//            StreamResult streamResult = new StreamResult(String.valueOf(domSource));
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.transform(domSource, streamResult);

            pdfDir.mkdirs();
            File pdfFile = new File(pdfDir, "invoice_"+ form.getOrderId() +".pdf");
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
//            // Setup output
//            OutputStream out = new FileOutputStream(pdfFile);
//            out = new java.io.BufferedOutputStream(out);

            OutputStream outfile = new FileOutputStream(pdfFile);
            outfile = new java.io.BufferedOutputStream(outfile);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                // Construct fop with desired output format
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                // Setup input for XSLT transformation
//                Source src = new StreamSource(String.valueOf(domSource));
                Source src = convert(xmlEncodedString);
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);

                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outfile);
                res = new SAXResult(fop.getDefaultHandler());
                transformer.transform(src, res);

            } catch (FOPException | TransformerException e) {
                throw new ApiException(e.getMessage());
            } finally {
                byte[] pdf = out.toByteArray();
                String base64 = Base64.getEncoder().encodeToString(pdf);
                Path path = Paths.get(pdfFile.getAbsolutePath());
                Files.write(path, pdf);
                outfile.close();
                return base64;
            }
        } catch(Exception exp){
            throw new ApiException(exp.getMessage());
        }
    }


    private static StreamSource convert(String base64EncodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
        return new StreamSource(bais);
    }

}