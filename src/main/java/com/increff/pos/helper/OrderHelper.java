package com.increff.pos.helper;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import org.hibernate.criterion.Order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class OrderHelper {

    public static OrderData convert(OrderPojo p) {
        OrderData d = new OrderData();
        d.setId(p.getId());
        d.setTime(LocalDateTime.ofInstant(p.getTime().toInstant(), ZoneOffset.ofHoursMinutes(5, 30)));
        //d.setTime(p.getTime());
        return d;
    }

    public static OrderPojo convert(OrderForm form) {
        return new OrderPojo();
    }
}
