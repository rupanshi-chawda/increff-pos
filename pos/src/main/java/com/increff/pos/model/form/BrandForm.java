package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BrandForm {


    @NotBlank
    @Size(min = 1, max = 15,message = " must be between 1 and 15 characters long ")
    private String brand;

    @NotBlank
    @Size(min = 1, max = 15,message = " must be between 1 and 15 characters long ")
    private String category;

}
