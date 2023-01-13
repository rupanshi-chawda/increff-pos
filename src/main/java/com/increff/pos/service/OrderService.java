package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {
    
    @Autowired
    private OrderDao dao;
    
    public void add(OrderPojo p) throws ApiException {
        dao.insert(p);
    }
    
    public OrderPojo get(int id) throws ApiException {
        return getOrderId(id);
    }

    public List<OrderPojo> getAll() {
        return dao.selectAll(OrderPojo.class);
    }

    private OrderPojo getOrderId(int id) throws ApiException {
        OrderPojo p = dao.selectById(id, OrderPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Order with given ID does not exit, id: " + id);
        }
        return p;
    }

}
