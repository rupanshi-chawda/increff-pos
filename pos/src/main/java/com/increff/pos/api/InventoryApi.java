package com.increff.pos.api;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryApi {

    @Autowired
    private InventoryDao dao;

    public void add(InventoryPojo p) throws ApiException {
        InventoryPojo b = getByInventoryId(p.getId());
        if(Objects.isNull(b)) {
            dao.insert(p);
        }
        else {
            Integer prevQuantity = b.getQuantity();
            Integer newQuantity = prevQuantity + p.getQuantity();
            b.setQuantity(newQuantity);
        }
    }

    public InventoryPojo get(Integer id) throws ApiException {
        InventoryPojo p = getByInventoryId(id);
        InventoryHelper.validateInventoryId(p);
        return p;
    }

    public List<InventoryPojo> getAll() {
        return dao.selectAll(InventoryPojo.class);
    }

    public void update(InventoryPojo p) throws ApiException {
        InventoryPojo bx = getByInventoryId(p.getId());
        InventoryHelper.validateId(bx, p.getId());
        bx.setQuantity(p.getQuantity());

    }

    // Business Logic Methods

    public InventoryPojo getByInventoryId(Integer id) {
        return dao.selectById(id, InventoryPojo.class);
    }

    public Integer getQuantityById(Integer id) {
        InventoryPojo p = getByInventoryId(id);
        return p.getQuantity();
    }
}
