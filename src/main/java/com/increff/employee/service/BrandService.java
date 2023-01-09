package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dto.BrandDto;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandDto dto;

    @Autowired
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo p) throws ApiException {
        BrandDto.normalize(p);
        dto.checkEmpty(p);
        dao.insert(dto.checkExists(p));
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int id) throws ApiException {
        return dto.checkId(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, BrandPojo p) throws ApiException {
        BrandDto.normalize(p);
        BrandPojo bx = dto.checkId(id);
        dto.checkUpdate(p.getBrand(), p.getCategory());
        bx.setCategory(p.getCategory());
        bx.setBrand(p.getBrand());
        dao.update(p);
    }

}
