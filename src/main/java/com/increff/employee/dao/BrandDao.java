package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {

    private static final String SELECT_BY_BRAND_CATEGORY = "select p from BrandPojo p where brand=:brand and " +
            "category=:category";
    private static final String SELECT_BY_ID = "select p from BrandPojo p where id=:id";
    private static final String SELECT_ALL = "select p from BrandPojo p";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(BrandPojo p){
        em.persist(p);
    }

    public BrandPojo select(int id) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_ID, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo selectComp(String brand, String category){
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand); 
        query.setParameter("category", category);
        return getSingle(query);
    }

    public void update(BrandPojo p) {
    }
}
