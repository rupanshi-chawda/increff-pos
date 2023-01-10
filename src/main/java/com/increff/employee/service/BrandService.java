package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class BrandService {

    @Autowired
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo p) throws ApiException {
        dao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int id) throws ApiException {
        return getBrandId(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return dao.selectAll(BrandPojo.class, "BrandPojo");
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, BrandPojo p) throws ApiException {
        BrandPojo bx = getBrandId(id);
        bx.setCategory(p.getCategory());
        bx.setBrand(p.getBrand());
        dao.update(p);
    }

    @Transactional
    public BrandPojo getBrandId(int id) throws ApiException {
        BrandPojo p = dao.selectById(id, BrandPojo.class, "BrandPojo");
        if (Objects.isNull(p)) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return p;
    }

    @Transactional
    public BrandPojo getBrandCategory(BrandPojo p) throws ApiException {
        BrandPojo b = dao.selectBrandCategory(p.getBrand(), p.getCategory());
        if(!Objects.isNull(b)) {
            throw new ApiException("Brand Category already exists");
        }
        return p;
    }

    @Transactional
    public BrandPojo getBrandCategory(String brand, String category) {
        return dao.selectBrandCategory(brand, category);
    }
}
