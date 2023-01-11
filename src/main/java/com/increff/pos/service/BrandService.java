package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

    @Autowired
    private BrandDao dao;

    public void add(BrandPojo p) throws ApiException {
        dao.insert(p);
    }

    public BrandPojo get(int id) throws ApiException {
        return getBrandId(id);
    }

    public List<BrandPojo> getAll() {
        return dao.selectAll(BrandPojo.class);
    }

    public void update(int id, BrandPojo p) throws ApiException {
        BrandPojo bx = getBrandId(id);
        bx.setCategory(p.getCategory());
        bx.setBrand(p.getBrand());
        dao.update(p);
    }

    public BrandPojo getBrandId(int id) throws ApiException {
        BrandPojo p = dao.selectById(id, BrandPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return p;
    }

    public BrandPojo getBrandCategory(BrandPojo p) throws ApiException {
        BrandPojo b = dao.selectBrandCategory(p.getBrand(), p.getCategory());
        if(!Objects.isNull(b)) {
            throw new ApiException("Brand Category already exists");
        }
        return p;
    }

    public BrandPojo getBrandCategory(String brand, String category) {
        return dao.selectBrandCategory(brand, category);
    }

    public int getBrandCategoryId(String brand, String category) {
        BrandPojo p = dao.selectBrandCategory(brand, category);
        return p.getId();
    }
}
