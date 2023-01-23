package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class ProductPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Barcode cannot be Empty")
    @Size(max = 8, min = 8)
    private String barcode;

    @NotNull(message = "Brand Category Id cannot be Empty")
    private int brandCategory;

    @NotNull(message = "Name cannot be Empty")
    private String name;

    @NotNull(message = "MRP cannot be Empty")
    @Min(value = 1)
    private Double mrp;

}
