package com.increff.invoice.util;

import com.increff.invoice.model.InvoiceForm;
import com.increff.invoice.model.OrderItemData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class XmlUtil {

    public static Document createDocumentElements(Document document, InvoiceForm invoiceForm){

        Double totalAmount = 0.0;

        // root element
        Element root = document.createElement("invoice");
        document.appendChild(root);

        Element orderId = document.createElement("order_id");
        orderId.appendChild(document.createTextNode(invoiceForm.getOrderId().toString()));
        root.appendChild(orderId);

        Element orderDate = document.createElement("order_date");
        String placed = invoiceForm.getPlaceDate();
        ZonedDateTime zonedPlaced = ZonedDateTime.parse(placed);
        LocalDateTime localPlaced = zonedPlaced.toLocalDateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedPlaced = localPlaced.format(dateTimeFormatter);
        orderDate.appendChild(document.createTextNode(formattedPlaced));
        root.appendChild(orderDate);

        // order item element
        for (OrderItemData o : invoiceForm.getOrderItemList()){
            Element orderItem = document.createElement("order_item");
            root.appendChild(orderItem);

            // set an attribute to staff element
            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(o.getOrderItemId().toString()));
            orderItem.appendChild(id);

            // firstname element
            Element productId = document.createElement("product_name");
            productId.appendChild(document.createTextNode(o.getProductName()));
            orderItem.appendChild(productId);

            // lastname element
            Element quantity = document.createElement("quantity");
            quantity.appendChild(document.createTextNode(o.getQuantity().toString()));
            orderItem.appendChild(quantity);

            Element sellingPrice = document.createElement("selling_price");
            sellingPrice.appendChild(document.createTextNode(valueFormatter(o.getSellingPrice())));
            orderItem.appendChild(sellingPrice);

            Double currentAmount = 0.0;
            currentAmount = o.getSellingPrice() * o.getQuantity();
            Element multiplied = document.createElement("multiplied");
            multiplied.appendChild(document.createTextNode(valueFormatter(currentAmount)));
            orderItem.appendChild(multiplied);

            totalAmount += currentAmount;
        }

        Element amount = document.createElement("amount");
        amount.appendChild(document.createTextNode(valueFormatter(totalAmount)));
        root.appendChild(amount);

        return document;
    }

    private static String valueFormatter(double value)
    {
        DecimalFormat formatter;
        if(value - (int)value > 0.0)
            formatter = new DecimalFormat("0.00");
        else
            formatter = new DecimalFormat("0");
        return formatter.format(value);
    }
}

