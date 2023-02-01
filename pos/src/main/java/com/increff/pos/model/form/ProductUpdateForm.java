package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductUpdateForm {

    @NotBlank
    private String name;


    @NotNull
    @Min(value = 1, message = "Mrp must be atleast 1")
    private Double mrp;

}
