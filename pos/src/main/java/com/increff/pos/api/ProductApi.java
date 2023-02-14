package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductApi {

    @Autowired
    private ProductDao dao;

    public void add(ProductPojo p) throws ApiException {
        dao.insert(p);
    }

    public ProductPojo get(Integer id) throws ApiException {
        return getProductId(id);
    }

    public List<ProductPojo> getAll() {
        return dao.selectAll(ProductPojo.class);
    }

    public void update(Integer id, ProductPojo p) throws ApiException {
        ProductPojo bx = getProductId(id);
        bx.setName(p.getName());
        bx.setMrp(p.getMrp());
        dao.update(p);
    }

    // Business Logic Methods

    public ProductPojo getProductId(Integer id) throws ApiException {
        ProductPojo p = dao.selectById(id, ProductPojo.class);
        if (Objects.isNull(p)) {
            throw new ApiException("Product with given ID does not exists, id: " + id);
        }
        return p;
    }

    public void checkBarcodeExists(ProductPojo p) throws ApiException {
        ProductPojo d = dao.selectByBarcode(p.getBarcode());
        if (!Objects.isNull(d)) {
            throw new ApiException("Product with given barcode already exists");
        }
    }

    public void checkProductBarcode(String barcode) throws ApiException {
        ProductPojo d = dao.selectByBarcode(barcode);
        if (Objects.isNull(d)) {
            throw new ApiException("Product with given barcode does not exists");
        }
    }

    public Integer getIdByBarcode(String barcode) {
        ProductPojo p = dao.selectByBarcode(barcode);
        return p.getId();
    }

    public String getBarcodeById(Integer id) {
        ProductPojo p = dao.selectById(id, ProductPojo.class);
        return p.getBarcode();
    }

    public List<ProductPojo> selectInId(Set<Integer> productIdList) throws ApiException {
        List<ProductPojo> productPojoList = dao.selectInId(productIdList);
        String error = "Following ID not found in Product Database: ";
        for (Integer id : productIdList) {
            if (!productPojoList.stream().anyMatch(productPojo -> productPojo.getId().equals(id))) {
                error += id + ", ";
            }
        }
        if(productPojoList.size() != productIdList.size()){
            throw new ApiException(error);
        }
        return productPojoList;
    }
}
