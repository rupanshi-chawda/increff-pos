package com.increff.employee.dto;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class BrandDto {

    @Autowired
    private BrandService service;

    public void add(BrandForm form) throws ApiException {
        BrandPojo p = convert(form);
        normalize(p);
        checkEmpty(p);
        p = service.getBrandCategory(p);
        service.add(p);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo p = service.get(id);
        return convert(p);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> list = service.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(int id, BrandForm f) throws ApiException {
        BrandPojo p = convert(f);
        normalize(p);
        checkUpdate(p.getBrand(), p.getCategory());
        service.update(id, p);
    }

    //Conversion and Normalization methods --------------------------------------

    private static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        d.setId(p.getId());
        return d;
    }

    private static BrandPojo convert(BrandForm f) {
        BrandPojo p = new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

    public void checkEmpty(BrandPojo p) throws ApiException {
        if(StringUtil.isEmpty(p.getBrand())) {
            throw new ApiException("Brand cannot be empty");
        }
        if(StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("Category cannot be empty");
        }
    }

    public void checkUpdate(String brand, String category) throws ApiException {
        BrandPojo b = service.getBrandCategory(brand, category);
        if(!Objects.isNull(b)) {
            throw new ApiException("Brand and Category already exist");
        }
    }

    public static void normalize(BrandPojo p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
    }
}
