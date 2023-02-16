package com.increff.pos.dao;

import com.increff.pos.pojo.SalesPojo;
import com.increff.pos.pojo.SalesPojo;
import com.increff.pos.pojo.SalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SalesDao extends AbstractDao {

    public List<SalesPojo> selectBetweenDates(LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<SalesPojo> q = cb.createQuery(SalesPojo.class);
        Root<SalesPojo> c = q.from(SalesPojo.class);

        q.select(c);
        ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class);
        ParameterExpression<LocalDate> a = cb.parameter(LocalDate.class);
        q.where(cb.between(c.get("date"), p, a));

        TypedQuery<SalesPojo> query = em().createQuery(q);
        query.setParameter(p, startDate);
        query.setParameter(a, endDate);
        return query.getResultList();
    }

    public SalesPojo selectByDate(LocalDate date) {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<SalesPojo> q = cb.createQuery(SalesPojo.class);
        Root<SalesPojo> c = q.from(SalesPojo.class);
        ParameterExpression<LocalDate> p = cb.parameter(LocalDate.class);
        q.select(c).where(cb.equal(c.get("date"), p));

        TypedQuery<SalesPojo> query = em().createQuery(q);
        query.setParameter(p, date);
        return getSingle(query);
    }
}
