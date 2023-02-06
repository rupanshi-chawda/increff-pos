package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductForm {

    @NotBlank
    @Size(min = 1, message = "must 8 character long")
    private String barcode;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 1, message = "must be atleast 1")
    private Double mrp;

}
