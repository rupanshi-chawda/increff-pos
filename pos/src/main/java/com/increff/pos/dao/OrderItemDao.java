package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {

    public void update(OrderItemPojo op){}

    public List<OrderItemPojo> selectByOrderId(Integer orderId) {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<OrderItemPojo> q = cb.createQuery(OrderItemPojo.class);
        Root<OrderItemPojo> c = q.from(OrderItemPojo.class);
        ParameterExpression<Integer> p = cb.parameter(Integer.class);
        q.select(c).where(cb.equal(c.get("orderId"), p));

        TypedQuery<OrderItemPojo> query = em().createQuery(q);
        query.setParameter(p, orderId);
        return query.getResultList();
    }
}
