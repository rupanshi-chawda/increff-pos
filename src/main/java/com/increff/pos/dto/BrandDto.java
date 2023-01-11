package com.increff.pos.dto;

import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@Service
public class BrandDto {

    @Autowired
    private BrandService service;

    public void add(BrandForm form) throws ApiException {
        BrandHelper.normalize(form);
        BrandHelper.validate(form);
        BrandPojo p = BrandHelper.convert(form);
        p = service.getBrandCategory(p);
        service.add(p);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo p = service.get(id);
        return BrandHelper.convert(p);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> list = service.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(BrandHelper.convert(p));
        }
        return list2;
    }

    public void update(int id, BrandForm f) throws ApiException {
        BrandHelper.normalize(f);
        checkUpdate(f.getBrand(), f.getCategory());
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
