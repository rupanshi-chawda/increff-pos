package com.increff.pos.model.data;

import com.increff.pos.model.form.OrderForm;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderData extends OrderForm {
    private int id;
    private Timestamp time;
}
