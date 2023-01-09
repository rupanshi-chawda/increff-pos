package com.increff.employee.service;

import com.increff.employee.dao.ProductDao;
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
    private ProductDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException {
        normalize(p);
        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if(p.getMrp()==0) {
            throw new ApiException("MRP cannot be empty or zero");
        }
        dao.insert(getCheck(p));
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {
        normalize(p);
        ProductPojo b = getCheck(id);
        b.setBarcode(p.getBarcode());
        b.setName(p.getName());
        b.setMrp(p.getMrp());
        dao.update(b);
    }
    
    // Checkers and Normalizers
    @Transactional
    public ProductPojo getCheck(int id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Employee with given ID does not exit, id: " + id);
        }
        return p;
    }

    @Transactional
    public ProductPojo getCheck(ProductPojo p) throws ApiException {
        ProductPojo b = dao.selectComp(p.getBrandCategory());
        if( Objects.isNull(b) ) {
            throw new ApiException("Brand Category already exists");
        }
        return p;
    }
    
    protected static void normalize(ProductPojo p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setName(StringUtil.toLowerCase(p.getName()));
    }
}
