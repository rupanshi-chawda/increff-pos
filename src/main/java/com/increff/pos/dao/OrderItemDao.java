package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class OrderItemDao extends AbstractDao{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(OrderItemPojo op) {
        em.persist(op);
    }

    public void update(OrderItemPojo op){}
}
