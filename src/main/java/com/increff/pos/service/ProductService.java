package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {

    @Autowired
    private ProductDao dao;

    @Autowired
    private BrandService brandService;

    public void add(ProductPojo p, String brand, String category) throws ApiException {
        p = getProductBarcode(p);
        p.setBrandCategory(brandService.getBrandCategoryId(brand, category));
        dao.insert(p);
    }

    public ProductPojo get(int id) throws ApiException {
        return getProductId(id);
    }

    public List<ProductPojo> getAll() {
        return dao.selectAll(ProductPojo.class);
    }

    public void update(int id, ProductPojo p) throws ApiException {
        ProductPojo bx = getProductId(id);
        bx.setBarcode(p.getBarcode());
        bx.setName(p.getName());
        bx.setMrp(p.getMrp());
        dao.update(p);
    }

    public ProductPojo getProductId(int id) throws ApiException {
        ProductPojo p = dao.selectById(id, ProductPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return p;
    }

    public ProductPojo getProductBarcode(ProductPojo p) throws ApiException {
        ProductPojo d = dao.selectBarcode(p.getBarcode(), ProductPojo.class);
        if (!Objects.isNull(d)) {
            throw new ApiException("Product with given barcode already exists");
        }
        return p;
    }

    public int getIdByBarcode(String barcode) {
        ProductPojo p = dao.selectBarcode(barcode, ProductPojo.class);
        return p.getId();
    }

    public String getBarcodeById(int id) {
        ProductPojo p = dao.selectById(id, ProductPojo.class);
        return p.getBarcode();
    }
}
