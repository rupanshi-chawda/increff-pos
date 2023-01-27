package com.increff.invoice.api;

import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PdfFromFop {
    public void createPDF() {
        try {
            File xmlfile = new File("C:\\Users\\KIIT\\Downloads\\increff-pos\\invoice\\src\\main\\resources\\xml\\invoice.xml");
            File xsltfile = new File("C:\\Users\\KIIT\\Downloads\\increff-pos\\invoice\\src\\main\\resources\\xsl\\invoice.xsl");
            File pdfDir = new File("./Pdf");
            pdfDir.mkdirs();
            File pdfFile = new File(pdfDir, "invoice.pdf");
            System.out.println(pdfFile.getAbsolutePath());
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
            // Setup output
            OutputStream out = new FileOutputStream(pdfFile);
            out = new java.io.BufferedOutputStream(out);
            try {
                // Construct fop with desired output format
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                out.close();
            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

}