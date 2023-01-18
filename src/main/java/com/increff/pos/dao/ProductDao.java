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
    private static final String SELECT_BY_BARCODE = "select p from ProductPojo p where barcode=:barcode";

    public ProductPojo selectBrandCategory(int brandCategory){
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY, ProductPojo.class);
        query.setParameter("brandCategory", brandCategory);
        return getSingle(query);
    }
    public ProductPojo selectBarcode(String barcode){
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public void update(ProductPojo p){}
}
