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
@Table(indexes = @Index(name = "barcodeIndex", columnList = "barcode", unique = true))
public class ProductPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Size(max = 8, min = 8)
    private String barcode;

    @Column(nullable = false)
    private Integer brandCategory;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Min(value = 1)//todo: make min 0
    private Double mrp;

}
