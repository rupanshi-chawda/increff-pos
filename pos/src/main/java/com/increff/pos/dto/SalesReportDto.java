package com.increff.pos.dto;

import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.util.CsvFileGenerator;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class SalesReportDto {
    @Autowired
    private OrderApi orderApi;

    @Autowired
    private BrandApi brandApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private CsvFileGenerator csvGenerator;

    protected List<SalesReportData> salesListData = new ArrayList<>();

    public List<SalesReportData> getAll() throws ApiException {
        List<OrderPojo> list = orderApi.getAllOrder();
        return getFilterSalesReport(list, "all", "all");
    }

    public List<SalesReportData> getFilterAll(SalesReportForm form) throws ApiException {

        ValidationUtil.validateForms(form);

        String startDate = form.getStartDate() + " 00:00:00";
        String endDate = form.getEndDate() + " 23:59:59";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime sDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime eDate = LocalDateTime.parse(endDate, formatter);

        ZonedDateTime start = sDate.atZone(ZoneId.systemDefault());
        ZonedDateTime end = eDate.atZone(ZoneId.systemDefault());;
        checkDates(start, end);

        List<OrderPojo> list = orderApi.getOrderByDateFilter(start, end);
        return getFilterSalesReport(list, form.getBrand(), form.getCategory());
    }

    public List<SalesReportData> getFilterSalesReport(List<OrderPojo> list, String brand, String category) throws ApiException {
        HashMap<Integer, SalesReportData> map = new HashMap<Integer, SalesReportData>();
        for(OrderPojo orderPojo: list)
        {
            List<OrderItemPojo> itemList = orderApi.getOrderItemsByOrderId(orderPojo.getId());

            for (OrderItemPojo p: itemList)
            {
                ProductPojo productPojo = productApi.get(p.getProductId());
                BrandPojo brandPojo = brandApi.get(productPojo.getBrandCategory());

                if(!map.containsKey(brandPojo.getId())) {
                    map.put(brandPojo.getId(), new SalesReportData());
                }

                SalesReportData salesReportData = map.get(brandPojo.getId());

                salesReportData.setQuantity(salesReportData.getQuantity() + p.getQuantity() );
                salesReportData.setRevenue(salesReportData.getRevenue() + (p.getQuantity() * p.getSellingPrice()));

            }
        }

        List<SalesReportData> salesList = new ArrayList<>();
        for(Map.Entry<Integer, SalesReportData> entry: map.entrySet()) {
            BrandPojo bp = brandApi.get(entry.getKey());
            if((Objects.equals(brand,bp.getBrand()) || Objects.equals(brand,"all")) &&
                    (Objects.equals(category,bp.getCategory()) || Objects.equals(category,"all")))
            {
                SalesReportData d = entry.getValue();
                d.setBrand(bp.getBrand());
                d.setCategory(bp.getCategory());
                salesList.add(d);
            }
        }

        salesListData = salesList;
        return salesListData;
    }


    public void generateCsv(HttpServletResponse response) throws ApiException {
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"salesReport.csv\"");
        try {
            csvGenerator.writeSalesToCsv(salesListData, response.getWriter());
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
        salesListData.clear();
    }

    private void checkDates(ZonedDateTime startDate, ZonedDateTime endDate) throws ApiException {
        if(endDate.isBefore(startDate)) {
            throw new ApiException("End date must not be before Start date");
        }
    }
}