package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class BrandDao extends AbstractDao {

    private static final String SELECT_BY_BRAND_AND_CATEGORY = "select p from BrandPojo p where brand=:brand and " +
            "category=:category";

    public BrandPojo selectBrandCategory(String brand, String category){
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_AND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand); 
        query.setParameter("category", category);
        return getSingle(query);
    }

    public void update(BrandPojo p) {
    }
}
