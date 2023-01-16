package com.increff.pos.dto;

import com.increff.pos.helper.OrderHelper;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Service
public class OrderDto {

    @Autowired
    private OrderService service;

    public void add(OrderForm form) throws ApiException {
        OrderPojo p = OrderHelper.convert(form);
        service.add(p);
    }

    public OrderData get(int id) throws ApiException {
        OrderPojo p = service.get(id);
        return OrderHelper.convert(p);
    }

    public List<OrderData> getAll() {
        return service.getAll().stream().map(p -> OrderHelper.convert(p)).collect(Collectors.toList());
    }
}
