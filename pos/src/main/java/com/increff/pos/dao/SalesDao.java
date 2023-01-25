package com.increff.pos.dao;

import com.increff.pos.pojo.SalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class SalesDao extends AbstractDao {

    private static final String SELECT_BTW_DATES = "select p from SalesPojo p where date>=:startDate and date<=:endDate";
    private static final String SELECT_BY_DATE = "select p from SalesPojo p where date=:date";

    public List<SalesPojo> selectBetweenDates(LocalDate startDate, LocalDate endDate) {
        TypedQuery<SalesPojo> query = getQuery(SELECT_BTW_DATES, SalesPojo.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public SalesPojo selectByDate(LocalDate date) {
        TypedQuery<SalesPojo> query = getQuery(SELECT_BY_DATE, SalesPojo.class);
        query.setParameter("date", date);
        return query.getResultStream().findFirst().orElse(null);
    }
}
