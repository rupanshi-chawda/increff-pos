package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

    private static final String SELECT_ALL_DESC = "select p from OrderPojo p order by time desc";

    public List<OrderPojo> selectAllDesc() {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL_DESC, OrderPojo.class);
        return query.getResultList();
    }
}
