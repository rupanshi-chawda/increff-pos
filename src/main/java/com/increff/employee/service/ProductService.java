package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.dto.ProductDto;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.ApiException;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductDto dto;

    @Autowired
    private ProductDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p, String brand, String category) throws ApiException {
        ProductDto.normalize(p);
        dto.checkEmpty(p);
        dao.insert(dto.checkExists(p, brand, category));
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException {
        return dto.checkId(id);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll(ProductPojo.class, "ProductPojo");
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {
        ProductDto.normalize(p);
        ProductPojo bx = dto.checkId(id);
        bx.setBarcode(p.getBarcode());
        bx.setName(p.getName());
        bx.setMrp(p.getMrp());
        dao.update(p);
    }

}
