package com.increff.pos.api;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandApi {

    @Autowired
    private BrandDao dao;

    public void add(BrandPojo p) throws ApiException {
        dao.insert(p);
    }

    public BrandPojo get(Integer id) throws ApiException {
        return getCheckBrandId(id);
    }

    public List<BrandPojo> getAllSorted() {
        return dao.selectAllSorted();
    }

    public void update(Integer id, BrandPojo p) throws ApiException {
        checkBrandCategoryExist(p);
        BrandPojo bx = getCheckBrandId(id);
        bx.setCategory(p.getCategory());
        bx.setBrand(p.getBrand());
        dao.update(p);
    }

    // Business Logic Methods

    public BrandPojo getCheckBrandId(Integer id) throws ApiException {
        BrandPojo p = dao.selectById(id, BrandPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Brand with given ID does not exists, id: " + id);
        }
        return p;
    }

    public void checkBrandCategoryExist(BrandPojo p) throws ApiException {
        BrandPojo b = getBrandCategory(p.getBrand(),p.getCategory());
        if(Objects.nonNull(b)) {
            throw new ApiException("Brand Category already exists");
        }
    }
    public Integer getCheckBrandCategoryId(String brand, String category) throws ApiException {
        BrandPojo existing = getBrandCategory(brand, category);
        if(Objects.isNull(existing)) {
            throw new ApiException("Brand and Category doesn't exist");
        }
        return existing.getId();
    }

    public BrandPojo getBrandCategory(String brand, String category) {
        return dao.selectBrandCategory(brand, category);
    }
}
