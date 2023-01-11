package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public class ProductDao extends AbstractDao{

    private static final String SELECT_BY_BRAND_CATEGORY = "select p from ProductPojo p where " +
            "brandCategory=:brandCategory";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(ProductPojo p) {
        em.persist(p);
    }

    public ProductPojo selectBrandCategory(int brandCategory){
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY, ProductPojo.class);
        query.setParameter("brandCategory", brandCategory);
        return getSingle(query);
    }

    public void update(ProductPojo p){}
}
