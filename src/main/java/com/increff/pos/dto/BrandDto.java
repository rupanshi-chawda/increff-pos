package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class BrandDto {

    @Autowired
    private BrandApi api;

    public void add(BrandForm form) throws ApiException {
        BrandHelper.normalize(form);
        BrandHelper.validate(form);
        BrandPojo p = BrandHelper.convert(form);
        api.add(p);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo p = api.get(id);
        return BrandHelper.convert(p);
    }

    public List<BrandData> getAll() {
        return api.getAll().stream().map(BrandHelper::convert).collect(Collectors.toList());
    }

    public void update(int id, BrandForm f) throws ApiException {
        BrandHelper.normalize(f);
        BrandHelper.validate(f);
        BrandPojo p = BrandHelper.convert(f);
        api.update(id, p);
    }
}
