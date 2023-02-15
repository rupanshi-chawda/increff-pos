package com.increff.pos.dto;

import com.increff.pos.api.OrderApi;
import com.increff.pos.api.SalesApi;
import com.increff.pos.model.form.SalesForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.SalesPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ValidationUtil;
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

    public List<SalesPojo> getAllBetweenDates(SalesForm salesForm) throws ApiException {
        LocalDate startdate = LocalDate.parse(salesForm.getStartDate());
        LocalDate enddate = LocalDate.parse(salesForm.getEndDate());
        ZonedDateTime startDate = startdate.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endDate = enddate.atStartOfDay(ZoneId.systemDefault());
        ValidationUtil.validateForms(salesForm);
        checkDates(startDate, endDate);
        return  api.getAllBetweenDates(startDate, endDate);
    }

    public List<SalesPojo> getAll() throws ApiException {
        return api.getAll();
    }

    public void createReport() {
        SalesPojo salesPojo = new SalesPojo();

        LocalDate date = LocalDate.now();
        Integer totalItems = 0;
        double totalRevenue = 0.0;

        ZonedDateTime startDate = date.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endDate = startDate.with(LocalTime.MAX);

        List<OrderPojo> orderPojoList = orderApi.getOrderByDateFilter(startDate,endDate);

        Integer totalOrders = orderPojoList.size();

        for (OrderPojo orderPojo : orderPojoList) {
            Integer id = orderPojo.getId();
            List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(id);
            for (OrderItemPojo orderItemPojo: orderItemPojoList) {
                totalItems += orderItemPojo.getQuantity();
                totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
            }
        }

        //todo helper
        salesPojo.setDate(startDate);
        salesPojo.setInvoicedItemsCount(totalItems);
        salesPojo.setTotalRevenue(totalRevenue);
        salesPojo.setInvoicedOrderCount(totalOrders);

        SalesPojo pojo = api.getByDate(startDate);
        if(Objects.isNull(pojo)){
            api.add(salesPojo);
        }
        else {
            api.update(startDate,salesPojo);
        }

    }

    private void checkDates(ZonedDateTime startDate, ZonedDateTime endDate) throws ApiException {
        if(endDate.isBefore(startDate)) {
            throw new ApiException("End date must not be before Start date");
        }
    }
}
