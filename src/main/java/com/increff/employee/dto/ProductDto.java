package com.increff.employee.dto;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.BrandForm;
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

    @Autowired
    private ProductDao dao;

    @Autowired
    private BrandDao brandDao;

    public void add(ProductForm form) throws ApiException {
        ProductPojo p = convert(form);
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
        service.update(id, p);
    }

    //Conversion and Normalization methods

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
        if(p.getMrp()<1) {
            throw new ApiException("MRP cannot be empty or zero");
        }
    }

    @Transactional
    public ProductPojo checkId(int id) throws ApiException {
        ProductPojo p = dao.selectById(id, ProductPojo.class, "ProductPojo");
        if (p == null) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return p;
    }

    @Transactional
    public ProductPojo checkExists(ProductPojo p, String brand, String category) throws ApiException {
        BrandPojo b = brandDao.selectComp(brand, category);
        if( Objects.isNull(b) ) {
            throw new ApiException("Brand Category doesn't exists");
        }
        p.setBrandCategory(b.getId());
        return p;
    }

    public static void normalize(ProductPojo p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setName(StringUtil.toLowerCase(p.getName()));
    }

}
