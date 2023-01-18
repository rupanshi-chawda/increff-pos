package com.increff.pos.dto;

import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Service
public class BrandDto {

    @Autowired
    private final BrandService service;

    public BrandDto(BrandService service) {
        this.service = service;
    }

    public void add(BrandForm form) throws ApiException {
        BrandHelper.normalize(form);
        BrandHelper.validate(form);
        BrandPojo p = BrandHelper.convert(form);
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
        BrandHelper.validate(f);
        BrandPojo p = BrandHelper.convert(f);
        service.update(id, p);
    }
}
