package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {

    private static final String SELECT_BY_BRAND_AND_CATEGORY = "select p from BrandPojo p where brand=:brand and " +
            "category=:category";
    private static final String SELECT_ALL = "select p from BrandPojo p order by brand asc, category asc";

    public BrandPojo selectBrandCategory(String brand, String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_AND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand); 
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<BrandPojo> selectAllSorted() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }

    public void update(BrandPojo p) {
    }
}
