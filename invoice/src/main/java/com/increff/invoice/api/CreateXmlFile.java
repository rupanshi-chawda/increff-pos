package com.increff.invoice.api;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.increff.invoice.model.*;
import com.increff.invoice.util.ApiException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXmlFile {
//todo: rename this file

    public String createXML(InvoiceForm invoiceForm) throws ApiException {
        Double totalAmount = 0.0;

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("invoice");
            document.appendChild(root);


            Element order_id = document.createElement("order_id");
            order_id.appendChild(document.createTextNode(invoiceForm.getOrderId().toString()));
            root.appendChild(order_id);

            Element order_date = document.createElement("order_date");
            String placed = invoiceForm.getPlaceDate();
            ZonedDateTime zonedPlaced = ZonedDateTime.parse(placed);
            LocalDateTime localPlaced = zonedPlaced.toLocalDateTime();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedPlaced = localPlaced.format(dateTimeFormatter);
            order_date.appendChild(document.createTextNode(formattedPlaced));
            root.appendChild(order_date);

            // order item element
            for (OrderItemData o : invoiceForm.getOrderItemList()){
                Element order_item = document.createElement("order_item");
                DecimalFormat df = new DecimalFormat("#.##");
                root.appendChild(order_item);

                // set an attribute to staff element
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(o.getOrderItemId().toString()));
                order_item.appendChild(id);

                // firstname element
                Element ProductId = document.createElement("product_name");
                ProductId.appendChild(document.createTextNode(o.getProductName()));
                order_item.appendChild(ProductId);

                // lastname element
                Element quantity = document.createElement("quantity");
                quantity.appendChild(document.createTextNode(o.getQuantity().toString()));
                order_item.appendChild(quantity);

                Element sellingPrice = document.createElement("selling_price");
                sellingPrice.appendChild(document.createTextNode(Double.valueOf(df.format(o.getSellingPrice())).toString()));
                order_item.appendChild(sellingPrice);

                Double currentAmount = 0.0;
                currentAmount = o.getSellingPrice() * o.getQuantity();
                Element multiplied = document.createElement("multiplied");
                multiplied.appendChild(document.createTextNode(Double.valueOf(df.format(currentAmount)).toString()));
                order_item.appendChild(multiplied);

                totalAmount += currentAmount;
            }

            Element amount = document.createElement("amount");
            DecimalFormat df = new DecimalFormat("#.##");
            amount.appendChild(document.createTextNode(Double.valueOf(df.format(totalAmount)).toString()));
            root.appendChild(amount);
            // create the xml file
            //transform the DOM Object to an XML File
            //DOMSource domSource = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(bos));
            byte[] xmlBytes = bos.toByteArray();
            String encodedXML = Base64.getEncoder().encodeToString(xmlBytes);

//            PdfFromFop pdfFromFOP = new PdfFromFop();
//            pdfFromFOP.createPDF(invoiceForm, encodedXML);

            return encodedXML;

        } catch (ParserConfigurationException pce) {
            throw new ApiException(pce.getMessage());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}