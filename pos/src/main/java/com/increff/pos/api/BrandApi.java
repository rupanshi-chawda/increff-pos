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

    public BrandPojo get(int id) throws ApiException {
        return getCheckBrandId(id);
    }

    public List<BrandPojo> getAll() {
        return dao.selectAll(BrandPojo.class);
    }

    public List<BrandPojo> getAllSorted() {
        return dao.selectAllSorted();
    }

    public void update(int id, BrandPojo p) throws ApiException {
        getCheckBrandCategory(p);
        BrandPojo bx = getCheckBrandId(id);
        bx.setCategory(p.getCategory());
        bx.setBrand(p.getBrand());
        dao.update(p);
    }

    // Business Logic Methods

    public BrandPojo getCheckBrandId(int id) throws ApiException {
        BrandPojo p = dao.selectById(id, BrandPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return p;
    }

    public void getCheckBrandCategory(BrandPojo p) throws ApiException {
        BrandPojo b = getBrandCategory(p.getBrand(),p.getCategory());
        if(!Objects.isNull(b)) {
            throw new ApiException("Brand Category already exists");
        }
    }
    public int checkBrandCategory(String brand, String category) throws ApiException {
        BrandPojo b = getBrandCategory(brand, category);
        if(Objects.isNull(b)) {
            throw new ApiException("Brand and Category doesn't exist");
        }
        return b.getId();
    }

    public BrandPojo getBrandCategory(String brand, String category) {
        return dao.selectBrandCategory(brand, category);
    }
}
