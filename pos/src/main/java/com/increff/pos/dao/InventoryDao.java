package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class InventoryDao extends AbstractDao {

    private static final String SELECT_BY_BARCODE = "select p from InventoryPojo p where barcode=:barcode";

    public void update(InventoryPojo p){}
}
