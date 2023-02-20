package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Repository
public class ProductDao extends AbstractDao{

    public ProductPojo selectByBarcode(String barcode) {
        CriteriaBuilder cb = em().getCriteriaBuilder();
        CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
        Root<ProductPojo> c = q.from(ProductPojo.class);
        ParameterExpression<String > p = cb.parameter(String.class);
        q.select(c).where(cb.equal(c.get("barcode"), p));

        TypedQuery<ProductPojo> query = em().createQuery(q);
        query.setParameter(p, barcode);
        return getSingle(query);
    }

}
