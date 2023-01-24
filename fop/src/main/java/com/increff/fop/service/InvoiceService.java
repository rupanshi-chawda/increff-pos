package com.increff.fop.service;

import com.increff.fop.model.InvoiceForm;
import com.increff.fop.model.OrderItemData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    public void generateInvoice(InvoiceForm form)
    {

        List<OrderItemData> items = form.getOrderItemList();
        Double amt = 0.0;
        for(OrderItemData i : items) {
            Double cur = 0.0;
            cur = i.getSellingPrice() * i.getQuantity();
            amt+=cur;
        }
        form.setAmount(amt);
        CreateXmlFile createXMLFileJava = new CreateXmlFile();

        createXMLFileJava.createXML(form);

        PdfFromFop pdfFromFOP = new PdfFromFop();

        pdfFromFOP.createPDF();
    }

    private void print(InvoiceForm form)
    {
        System.out.println(form.getOrderId());
        System.out.println(form.getPlaceDate());

        List<OrderItemData> orderItemList = form.getOrderItemList();

        System.out.println(orderItemList.size());

        for(OrderItemData o :orderItemList)
        {
            System.out.println(o.getProductName());
            System.out.println(o.getOrderItemId());
            System.out.println(o.getQuantity());
            System.out.println(o.getSellingPrice());
        }
    }

}