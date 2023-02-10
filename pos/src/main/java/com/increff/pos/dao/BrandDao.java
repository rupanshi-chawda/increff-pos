package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {

    public BrandPojo selectBrandCategory(String brand, String category) {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<BrandPojo> q = cb.createQuery(BrandPojo.class);
        Root<BrandPojo> c = q.from(BrandPojo.class);

        q.select(c);
        ParameterExpression<String> p = cb.parameter(String.class);
        ParameterExpression<String> a = cb.parameter(String.class);
        q.where(
                cb.equal(c.get("brand"), p),
                cb.equal(c.get("category"), a)
        );

        TypedQuery<BrandPojo> query = em().createQuery(q);
        query.setParameter(p, brand);
        query.setParameter(a, category);
        return getSingle(query);
    }

    public List<BrandPojo> selectAllSorted() {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<BrandPojo> q = cb.createQuery(BrandPojo.class);
        Root<BrandPojo> c = q.from(BrandPojo.class);
        q.select(c);
        q.orderBy(cb.asc(c.get("brand")), cb.asc(c.get("category")));
        
        TypedQuery<BrandPojo> query = em().createQuery(q);
        return query.getResultList();
    }

    public void update(BrandPojo p) {
    }
}
