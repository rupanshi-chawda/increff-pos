package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
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
        return getInventoryId(id);
    }

    public List<InventoryPojo> getAll() {
        return dao.selectAll(InventoryPojo.class);
    }

    public void update(int id, InventoryPojo p) throws ApiException {
        InventoryPojo bx = getInventoryId(id);
        bx.setQuantity(p.getQuantity());
        dao.update(p);
    }

    public InventoryPojo getInventoryId(int id) throws ApiException {
        InventoryPojo p = dao.selectById(id, InventoryPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return p;
    }
}
