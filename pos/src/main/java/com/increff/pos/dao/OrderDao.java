package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

    public List<OrderPojo> selectAllDesc() {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
        Root<OrderPojo> c = q.from(OrderPojo.class);
        q.select(c);
        q.orderBy(cb.desc(c.get("time")));

        TypedQuery<OrderPojo> query = em().createQuery(q);
        return query.getResultList();
    }

    public List<OrderPojo> selectOrderByDateFilter(ZonedDateTime startDate, ZonedDateTime endDate) {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
        Root<OrderPojo> c = q.from(OrderPojo.class);

        q.select(c);
        ParameterExpression<ZonedDateTime> p = cb.parameter(ZonedDateTime.class);
        ParameterExpression<ZonedDateTime> a = cb.parameter(ZonedDateTime.class);
        q.where(cb.between(c.get("createdAt"), p, a));

        TypedQuery<OrderPojo> query = em().createQuery(q);
        query.setParameter(p, startDate);
        query.setParameter(a, endDate);
        return query.getResultList();
    }

    public void update(OrderPojo p) {
    }
}
