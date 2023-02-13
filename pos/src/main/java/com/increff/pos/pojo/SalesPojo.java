package com.increff.pos.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class SalesPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd ")
    @Column(nullable = false)
    private ZonedDateTime date;

    @Column(nullable = false)
    private Integer invoicedOrderCount;

    @Column(nullable = false)
    private Integer invoicedItemsCount;

    @Column(nullable = false)
    private Double totalRevenue;
}
