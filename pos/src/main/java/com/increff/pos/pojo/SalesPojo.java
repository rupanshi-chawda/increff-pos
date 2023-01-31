package com.increff.pos.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class SalesPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd ")
    @NotNull
    private LocalDate date;
    //TODO: try to add zonedatetime instead of localdate
    @NotNull
    private Integer invoicedOrderCount;

    @NotNull
    private Integer invoicedItemsCount;

    @NotNull
    private Double totalRevenue;
}