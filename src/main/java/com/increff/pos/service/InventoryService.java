package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao dao;

    @Autowired
    private ProductDao productDao;


}
