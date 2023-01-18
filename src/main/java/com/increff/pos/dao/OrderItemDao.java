package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{

    private static final String SELECT_BY_ORDER_ID = "select p from OrderItemPojo p where orderid=:orderid";

    public void update(OrderItemPojo op){}

    public List<OrderItemPojo> selectByOrderId(int orderid) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER_ID, OrderItemPojo.class);
        query.setParameter("orderid", orderid);
        return query.getResultList();
    }
}
