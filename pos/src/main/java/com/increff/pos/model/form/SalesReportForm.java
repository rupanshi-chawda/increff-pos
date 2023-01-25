package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SalesReportForm {

    @NotBlank(message = "StartDate Cannot be Empty")
    private String startDate;
    @NotBlank(message = "EndDate Cannot be Empty")
    private String endDate;
    @NotBlank(message = "Brand Cannot be Empty")
    private String brand;
    @NotBlank(message = "Category Cannot be Empty")
    private String category;

}
