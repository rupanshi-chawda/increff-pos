package com.increff.invoice.api;

import com.increff.invoice.util.ApiException;
import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Base64;

public class PdfFromFop {
    public String createPDF(String xmlEncodedString) throws ApiException {

            String path = new File("src/main/resources/xsl/invoice.xsl").getAbsolutePath();
            File xsltfile = new File(path);

            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                // Construct fop with desired output format
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

                byte[] pdf = out.toByteArray();
                String base64 = Base64.getEncoder().encodeToString(pdf);
                return base64;

            } catch (FOPException | TransformerException e) {
                throw new ApiException("Cannot Transform to create Invoice"+e.getMessage());
            }
    }


    private static StreamSource convert(String base64EncodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
        return new StreamSource(bais);
    }

}