package com.increff.invoice.api;


import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.increff.invoice.model.*;
import com.increff.invoice.util.ApiException;
import com.increff.invoice.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlGeneration {
    public String createXML(InvoiceForm invoiceForm) throws ApiException {

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            document = XmlUtil.createDocumentElements(document, invoiceForm);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(bos));
            byte[] xmlBytes = bos.toByteArray();
            String encodedXML = Base64.getEncoder().encodeToString(xmlBytes);

            return encodedXML;

        } catch (ParserConfigurationException | TransformerException pce) {
            throw new ApiException(pce.getMessage());
        }
    }
}