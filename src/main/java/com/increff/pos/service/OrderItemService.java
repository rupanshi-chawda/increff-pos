package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.helper.OrderItemHelper;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;

    @Autowired
    private ProductService productService;

    public void add(OrderItemPojo p, String barcode, int oid) throws ApiException {
        int id = productService.getIdByBarcode(barcode);
        p.setProductId(id);
        p.setOrderId(oid);
        dao.insert(p);
    }

    public OrderItemPojo get(int id) throws ApiException {
        OrderItemPojo p = getOrderItemId(id);
        OrderItemHelper.validateId(p, id);
        return p;
    }

    public List<OrderItemPojo> getAll() {
        return dao.selectAll(OrderItemPojo.class);
    }

    private OrderItemPojo getOrderItemId(int id) {
        return dao.selectById(id, OrderItemPojo.class);
    }
}
