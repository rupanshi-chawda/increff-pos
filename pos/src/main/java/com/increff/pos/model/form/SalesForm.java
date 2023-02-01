package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SalesForm {

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

}
