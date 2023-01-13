package com.increff.pos.dto;

import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@Service
public class BrandDto {

    @Autowired
    private BrandService service;

    public void add(BrandForm form) throws ApiException {
        BrandHelper.normalize(form);
        BrandHelper.validate(form);
        BrandPojo p = BrandHelper.convert(form);
        p = service.getCheckBrandCategory(p);
        service.add(p);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo p = service.get(id);
        return BrandHelper.convert(p);
    }

    public List<BrandData> getAll() {
        return service.getAll().stream().map(p -> BrandHelper.convert(p)).collect(Collectors.toList());
    }

    public void update(int id, BrandForm f) throws ApiException {
        BrandHelper.normalize(f);
        checkUpdate(f.getBrand(), f.getCategory());
        BrandHelper.validate(f);
        BrandPojo p = BrandHelper.convert(f);
        service.update(id, p);
    }

    //Check Method
    public void checkUpdate(String brand, String category) throws ApiException {
        BrandPojo b = service.getBrandCategory(brand, category);
        if(!Objects.isNull(b)) {
            throw new ApiException("Brand and Category already exist");
        }
    }
}
