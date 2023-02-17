package com.increff.pos.helper;

import com.increff.pos.pojo.SalesPojo;

import java.time.LocalDate;

public class SalesJobHelper {

    public static SalesPojo createForm(LocalDate date, Integer totalItem, Double totalRevenue, Integer totalOrders) {
        SalesPojo salesPojo = new SalesPojo();
        salesPojo.setDate(date);
        salesPojo.setInvoicedItemsCount(totalItem);
        salesPojo.setTotalRevenue(totalRevenue);
        salesPojo.setInvoicedOrderCount(totalOrders);
        return salesPojo;
    }
}
