package com.increff.pos.dto;

import com.increff.pos.api.OrderApi;
import com.increff.pos.api.SalesApi;
import com.increff.pos.model.form.SalesForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.SalesPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Objects;

@Component
@Service
public class SalesDto {

    @Autowired
    private SalesApi api;

    @Autowired
    private OrderApi orderApi;

    public List<SalesPojo> getAllBetweenDates(SalesForm salesForm) {
        LocalDate startDate = LocalDate.parse(salesForm.getStartDate());
        LocalDate endDate = LocalDate.parse(salesForm.getEndDate());
        return  api.getAllBetweenDates(startDate, endDate);
    }

    public List<SalesPojo> getAll() throws ApiException {
        createReport();
        return api.getALL();
    }

    public void createReport() throws ApiException {
        SalesPojo salesPojo = new SalesPojo();

        LocalDate date = LocalDate.now();
        int totalItems = 0;
        double totalRevenue = 0.0;

        ZonedDateTime startDate = date.atStartOfDay(ZoneId.systemDefault());
        //LocalDateTime eldt =  LocalDateTime.of(date, LocalTime.MAX);
        ZonedDateTime endDate = startDate.with(LocalTime.MAX);

        List<OrderPojo> orderPojoList = orderApi.getOrderByDateFilter(startDate,endDate);

        Integer totalOrders = orderPojoList.size();

        for (OrderPojo orderPojo : orderPojoList) {
            int id = orderPojo.getId();
            List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(id);
            for (OrderItemPojo orderItemPojo: orderItemPojoList) {
                totalItems += orderItemPojo.getQuantity();
                totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
            }
        }

        salesPojo.setDate(date);
        salesPojo.setInvoicedItemsCount(totalItems);
        salesPojo.setTotalRevenue(totalRevenue);
        salesPojo.setInvoicedOrderCount(totalOrders);

        SalesPojo pojo = api.getByDate(date);
        if(Objects.isNull(pojo)){
            api.add(salesPojo);
        }
        else {
            api.update(date,salesPojo);
        }

    }
}
