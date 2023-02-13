package com.increff.invoice.api;

import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.util.ApiException;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class PdfFromFop {

    @Value("${xsl.path}")
    private String xsl_path;

    public String createPDF(InvoiceForm form, String xmlEncodedString) throws ApiException {

        File xsltfile = new File("C:\\Users\\KIIT\\Downloads\\increff-pos\\invoice\\src\\main\\resources\\xsl\\invoice.xsl");
        File pdfDir = new File("./Pdf");

        pdfDir.mkdirs();
        File pdfFile = new File(pdfDir, "invoice_" + form.getOrderId() + ".pdf");
        // configure fopFactory as desired
        final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        // Setup output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStream outfile = null;

        try {
            // Construct fop with desired output format
            outfile = new FileOutputStream(pdfFile);
            outfile = new java.io.BufferedOutputStream(outfile);

            Fop fop;
            fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

            // Setup input for XSLT transformation
            Source src = convert(xmlEncodedString);
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);

            fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outfile);
            res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);

        } catch (FOPException | TransformerException e) {
            throw new ApiException("FOP transformation not working" + e.getMessage());
        } finally {
            byte[] pdf = out.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(pdf);
            Path path = Paths.get(pdfFile.getAbsolutePath());
            try {
                Files.write(path, pdf);
                outfile.close();
            } catch (IOException e) {
                throw new ApiException("File cannot be created" + e.getMessage());
            }
            return base64;
        }

    }

    private static StreamSource convert(String base64EncodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
        return new StreamSource(bais);
    }

}