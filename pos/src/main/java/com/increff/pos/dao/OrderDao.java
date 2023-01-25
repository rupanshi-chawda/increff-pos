package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

    private static final String SELECT_ALL_DESC = "select p from OrderPojo p order by time desc";
    private static final String SELECT_BY_DATE_FILTER = "select p from OrderPojo p where createdAt>=:startDate and createdAt<=:endDate";

    public List<OrderPojo> selectAllDesc() {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL_DESC, OrderPojo.class);
        return query.getResultList();
    }

    public List<OrderPojo> selectOrderByDateFilter(ZonedDateTime startDate, ZonedDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BY_DATE_FILTER, OrderPojo.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }
}
