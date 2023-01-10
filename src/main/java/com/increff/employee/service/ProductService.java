package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductDao dao;

    @Autowired
    private BrandDao brandDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p, String brand, String category) throws ApiException {
        p = getProductBarcode(p);
        dao.insert(getProductBrandCategory(p, brand, category));
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException {
        return getProductId(id);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll(ProductPojo.class, "ProductPojo");
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {
        ProductPojo bx = getProductId(id);
        bx.setBarcode(p.getBarcode());
        bx.setName(p.getName());
        bx.setMrp(p.getMrp());
        dao.update(p);
    }

    @Transactional
    public ProductPojo getProductId(int id) throws ApiException {
        ProductPojo p = dao.selectById(id, ProductPojo.class, "ProductPojo");
        if (Objects.isNull(p)) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return p;
    }

    @Transactional
    public ProductPojo getProductBarcode(ProductPojo p) throws ApiException {
        ProductPojo d = dao.selectBarcode(p.getBarcode());
        if (!Objects.isNull(d)) {
            throw new ApiException("Product with given barcode already exists");
        }
        return p;
    }

    @Transactional
    public ProductPojo getProductBrandCategory(ProductPojo p, String brand, String category) throws ApiException {
        BrandPojo b = brandDao.selectBrandCategory(brand, category);
        if( Objects.isNull(b) ) {
            throw new ApiException("Brand Category doesn't exists");
        }
        p.setBrandCategory(b.getId());
        return p;
    }

}
