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
import java.text.DecimalFormat;
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
        HashMap<Integer, SalesReportData> salesMap = new HashMap<>();
        HashMap<Integer, BrandPojo> brandMap = getBrandMap();
        HashMap<Integer, ProductPojo> productMap = getProductMap(list);


        for(OrderPojo orderPojo: list)
        {
            List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(orderPojo.getId());
            DecimalFormat df = new DecimalFormat("#.##");
            for (OrderItemPojo orderItemPojo: orderItemPojoList) {
                ProductPojo productPojo = productMap.get(orderItemPojo.getProductId());
                BrandPojo brandPojo = brandMap.get(productPojo.getBrandCategory());
                if((Objects.equals(brand,brandPojo.getBrand()) || Objects.equals(brand,"all")) &&
                        (Objects.equals(category,brandPojo.getCategory()) || Objects.equals(category,"all"))) {
                    if(!salesMap.containsKey(brandPojo.getId())) {
                        salesMap.put(brandPojo.getId(), new SalesReportData());
                    }
                    SalesReportData salesReportData = salesMap.get(brandPojo.getId());
                    salesReportData.setQuantity(salesReportData.getQuantity() + orderItemPojo.getQuantity() );
                    salesReportData.setRevenue(Double.parseDouble(df.format(salesReportData.getRevenue()
                            + (orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice()))));
                }
            }
        }

        List<SalesReportData> salesReportDataList = new ArrayList<>();
        for(Map.Entry<Integer, SalesReportData> entry: salesMap.entrySet()) {
            BrandPojo bp = brandMap.get(entry.getKey());
            SalesReportData d = entry.getValue();
            d.setBrand(bp.getBrand());
            d.setCategory(bp.getCategory());
            salesReportDataList.add(d);
        }
        salesListData = salesReportDataList;
        return salesListData;
    }

    public void generateCsv(HttpServletResponse response) throws ApiException {
        response.setContentType("text/csv");

        LocalDateTime lt = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm");
        String identifier = lt.format(dateTimeFormatter);

        response.addHeader("Content-Disposition", "attachment; filename=\"salesReport"+identifier+".csv\"");
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

    private HashMap<Integer, ProductPojo> getProductMap(List<OrderPojo> list) throws ApiException {
        HashMap<Integer, ProductPojo> productMap = new HashMap<>();
        Set<Integer> productIdList = new HashSet<>();
        for (OrderPojo orderPojo: list) {
            List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(orderPojo.getId());
            for (OrderItemPojo orderItemPojo: orderItemPojoList) {
                productIdList.add(orderItemPojo.getProductId());
            }
        }
        List<ProductPojo> productPojoList = productApi.selectInId(productIdList);
        for (ProductPojo productPojo: productPojoList) {
            productMap.put(productPojo.getId(), productPojo);
        }
        return productMap;
    }
    //todo: optimise this function to reduce api calls
    private HashMap<Integer, BrandPojo> getBrandMap() {
        HashMap<Integer, BrandPojo> brandMap = new HashMap<>();
        List<BrandPojo> brandPojoList = brandApi.getAll();
        for (BrandPojo brandPojo: brandPojoList) {
            brandMap.put(brandPojo.getId(), brandPojo);
        }
        return brandMap;
    }

}