package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class OrderItemForm {


    @NotBlank
    @Size(min = 8, max = 8, message = "must 8 character long")
    private String barcode;

    @NotNull
    @Min(value = 1, message = "must be atleast 1")
    private Integer quantity;

    @NotNull
    @Min(value = 0, message = "must be atleast 0")
    private Double sellingPrice;

}
