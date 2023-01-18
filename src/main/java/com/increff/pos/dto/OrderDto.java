package com.increff.pos.dto;

import com.increff.pos.helper.OrderHelper;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class OrderDto {

    @Autowired
    private final OrderService service;

    public OrderDto(OrderService service) {
        this.service = service;
    }

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
