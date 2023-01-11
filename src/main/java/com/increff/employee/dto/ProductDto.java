package com.increff.employee.dto;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ApiException;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class ProductDto {

    @Autowired
    private ProductService service;

    public void add(ProductForm form) throws ApiException {
        ProductPojo p = convert(form);
        normalize(p);
        checkEmpty(p);
        service.add(p, form.getBrand(), form.getCategory());
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo p = service.get(id);
        return convert(p);
    }

    public List<ProductData> getAll() {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(int id, ProductForm f) throws ApiException {
        ProductPojo p = convert(f);
        normalize(p);
        service.update(id, p);
    }

    //Conversion and Normalization methods --------------------------------------

    private static ProductData convert(ProductPojo p) {
        ProductData d = new ProductData();
        d.setBrandCategory(p.getBrandCategory());
        d.setBarcode(p.getBarcode());
        d.setId(p.getId());
        d.setName(p.getName());
        d.setMrp(p.getMrp());
        return d;
    }

    private static ProductPojo convert(ProductForm f) {
        ProductPojo p = new ProductPojo();
        p.setBrandCategory(f.getBrandCategory());
        p.setName(f.getName());
        p.setBarcode(f.getBarcode());
        p.setMrp(f.getMrp());
        return p;
    }

    public void checkEmpty(ProductPojo p) throws ApiException {
        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if(p.getMrp()<1 || Objects.isNull(p.getMrp())) {
            throw new ApiException("MRP cannot be empty or less than one");
        }
    }

    public static void normalize(ProductPojo p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setName(StringUtil.toLowerCase(p.getName()));
    }

}
