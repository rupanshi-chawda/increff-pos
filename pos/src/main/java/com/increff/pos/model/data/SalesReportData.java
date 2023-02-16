package com.increff.pos.model.data;

import com.increff.pos.model.form.SalesReportForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportData extends SalesReportForm {

    private Integer quantity = 0;
    private Double revenue = 0.0;

}
