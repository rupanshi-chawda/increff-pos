package com.increff.pos.model.data;

import com.increff.pos.model.form.SalesReportForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportData extends SalesReportForm {

    private int quantity;
    private double revenue;

    public SalesReportData() {
        this.setQuantity(0);
        this.setRevenue(0.0);
    }
}