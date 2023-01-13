package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

    @Autowired
    private InventoryDao dao;

    public void add(InventoryPojo p) throws ApiException {
        InventoryPojo b = getInventoryId(p.getId());
        if(Objects.isNull(b)) {
            dao.insert(p);
        }
        else {
            //TODO: Add toaster message that the product already exists and that you are updating it.
            int prevQuantity = b.getQuantity();
            int newQuantity = prevQuantity + p.getQuantity();
            b.setQuantity(newQuantity);
            dao.update(p);
        }
    }

    public InventoryPojo get(int id) throws ApiException {
        InventoryPojo p = getInventoryId(id);
        InventoryHelper.validateId(p, id);
        return p;
    }

    public List<InventoryPojo> getAll() {
        return dao.selectAll(InventoryPojo.class);
    }

    public void update(int id, InventoryPojo p) throws ApiException {
        InventoryPojo bx = getInventoryId(id);
        InventoryHelper.validateId(bx, id);
        bx.setQuantity(p.getQuantity());
        dao.update(p);
    }

    public InventoryPojo getInventoryId(int id) {
        return dao.selectById(id, InventoryPojo.class);
    }

    public int getQuantityById(int id) {
        InventoryPojo p = getInventoryId(id);
        return p.getQuantity();
    }
}
