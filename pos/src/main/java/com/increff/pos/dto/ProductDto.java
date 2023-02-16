package com.increff.pos.dto;

import com.increff.pos.model.data.ProductErrorData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductUpdateForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ErrorUtil;
import com.increff.pos.util.ValidationUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ProductDto {

    @Autowired
    private ProductApi api;

    @Autowired
    private ProductFlowApi flowApi;

    @Autowired
    private BrandApi brandApi;

    public void add(List<ProductForm> forms) throws ApiException {
        checkDuplicates(forms);
        ProductHelper.validateFormList(forms);
        flowApi.add(forms);
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo p = api.get(id);
        BrandPojo b = brandApi.getCheckBrandId(p.getBrandCategory());
        return ProductHelper.convert(p, b);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = api.getAll();
        List<ProductData> list2 = new ArrayList<>();
        for(ProductPojo productPojo : list) {
            BrandPojo brandPojo= brandApi.getCheckBrandId(productPojo.getBrandCategory());
            list2.add(ProductHelper.convert(productPojo, brandPojo));
        }
        return list2;
    }

    public void update(Integer id, ProductUpdateForm f) throws ApiException {
        ProductHelper.normalize(f);
        ValidationUtil.validateForms(f);
        ProductPojo p = ProductHelper.convert(f);
        api.update(id, p);
    }

    private void checkDuplicates(List<ProductForm> forms) throws ApiException {
        HashSet<String> set = new HashSet<>();
        for(ProductForm form : forms) {
            String p = form.getBarcode();
            if (set.contains(p)) {
                throw new ApiException("Duplicate Barcode Exists");
            }
            set.add(p);
        }
    }
}