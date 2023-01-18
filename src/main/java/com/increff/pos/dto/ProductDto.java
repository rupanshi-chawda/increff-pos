package com.increff.pos.dto;

import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class ProductDto {

    @Autowired
    private final ProductService service;

    public ProductDto(ProductService service) {
        this.service = service;
    }

    public void add(ProductForm form) throws ApiException {
        ProductHelper.normalize(form);
        ProductHelper.validate(form);
        ProductPojo p = ProductHelper.convert(form);
        service.add(p, form.getBrand(), form.getCategory());
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo p = service.get(id);
        return ProductHelper.convert(p);
    }

    public List<ProductData> getAll() {
        return service.getAll().stream().map(p -> ProductHelper.convert(p)).collect(Collectors.toList());
    }

    public void update(int id, ProductForm f) throws ApiException {
        ProductHelper.normalize(f);
        ProductHelper.validate(f);
        ProductPojo p = ProductHelper.convert(f);
        service.update(id, p);
    }

}
