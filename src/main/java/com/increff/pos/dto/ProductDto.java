package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@Service
public class ProductDto {

    @Autowired
    private ProductService service;

    public void add(ProductForm form) throws ApiException {
        ProductHelper.normalize(form);
        ProductHelper.checkEmpty(form);
        ProductPojo p = ProductHelper.convert(form);
        service.add(p, form.getBrand(), form.getCategory());
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo p = service.get(id);
        return ProductHelper.convert(p);
    }

    public List<ProductData> getAll() {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo p : list) {
            list2.add(ProductHelper.convert(p));
        }
        return list2;
    }

    public void update(int id, ProductForm f) throws ApiException {
        ProductHelper.normalize(f);
        ProductPojo p = ProductHelper.convert(f);
        service.update(id, p);
    }

}
