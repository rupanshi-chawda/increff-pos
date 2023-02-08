package com.increff.pos.helper;


import com.increff.pos.model.form.SalesReportForm;

public class SalesReportTestHelper {

    public static SalesReportForm createForm(String startDate, String endDate, String brand, String category){
        SalesReportForm salesReportForm = new SalesReportForm();
        salesReportForm.setStartDate(startDate);
        salesReportForm.setEndDate(endDate);
        salesReportForm.setCategory(brand);
        salesReportForm.setBrand(category);
        return salesReportForm;
    }
}
