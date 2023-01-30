package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductUpdateForm {

    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "MRP cannot be empty")
    private Double mrp;

}
