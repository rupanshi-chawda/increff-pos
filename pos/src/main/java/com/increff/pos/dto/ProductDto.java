package com.increff.pos.dto;

import com.increff.pos.model.form.ProductForm;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductUpdateForm;
import com.increff.pos.pojo.ProductPojo;
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
    private ProductApi api;

    @Autowired
    private BrandApi brandApi;

    public void add(ProductForm form) throws ApiException {
        ProductHelper.normalize(form);
        ProductPojo p = ProductHelper.convert(form);
        p.setBrandCategory(brandApi.getBrandCategoryId(form.getBrand(), form.getCategory()));
        api.add(p);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo p = api.get(id);
        return ProductHelper.convert(p);
    }

    public List<ProductData> getAll() {
        return api.getAll().stream().map(ProductHelper::convert).collect(Collectors.toList());
    }

    public void update(int id, ProductUpdateForm f) throws ApiException {
        ProductHelper.normalize(f);
        ProductPojo p = ProductHelper.convert(f);
        api.update(id, p);
    }

}
