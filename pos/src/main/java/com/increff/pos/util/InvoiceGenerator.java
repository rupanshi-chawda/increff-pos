package com.increff.pos.util;

import com.increff.pos.model.data.OrderItem;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.ProductApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceGenerator {

    @Autowired
    OrderApi orderApi;

    @Autowired
    ProductApi productApi;

    public InvoiceForm generateInvoiceForOrder(int orderId) throws ApiException
    {
        InvoiceForm invoiceForm = new InvoiceForm();
        OrderPojo orderPojo = orderApi.getOrder(orderId);

        invoiceForm.setOrderId(orderPojo.getId());
        invoiceForm.setPlaceDate(orderPojo.getTime().toString());

        List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemByOrderItem(orderPojo.getId());
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderItemPojo p: orderItemPojoList)
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(p.getId());
            String productName = productApi.get(p.getProductId()).getName();
            orderItem.setProductName(productName);
            orderItem.setQuantity(p.getQuantity());
            orderItem.setSellingPrice(p.getSellingPrice());
            orderItemList.add(orderItem);
        }

        invoiceForm.setOrderItemList(orderItemList);

        return invoiceForm;
    }
}