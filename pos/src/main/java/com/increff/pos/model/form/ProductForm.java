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
    @Size(min = 8, max = 8, message = "must 8 character long")
    private String barcode;

    @NotBlank
    @Size(min = 1, max = 15,message = " must be between 1 and 15 characters long ")
    private String brand;

    @NotBlank
    @Size(min = 1, max = 15,message = " must be between 1 and 15 characters long ")
    private String category;

    @NotBlank
    @Size(min = 1, max = 25,message = " must be between 1 and 25 characters long ")
    private String name;

    @NotNull
    @Min(value = 0, message = "must be atleast 0")
    private Double mrp;

}
