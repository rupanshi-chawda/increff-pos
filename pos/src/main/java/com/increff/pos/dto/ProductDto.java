package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.model.data.ProductErrorData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductUpdateForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ErrorUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class ProductDto {

    @Autowired
    private ProductApi api;

    @Autowired
    private BrandApi brandApi;

    public void add(List<ProductForm> forms) throws ApiException, JsonProcessingException {
        List<ProductErrorData> errorData = new ArrayList<>();
        errorData.clear();
        int errorSize = 0;
        for(ProductForm f: forms)
        {
            ProductErrorData productErrorData = ConvertUtil.convert(f, ProductErrorData.class);
            productErrorData.setMessage("");
            try
            {
                ValidationUtil.validateForms(f);
                ProductHelper.normalize(f);
                ProductPojo p = ProductHelper.convert(f);
                p.setBrandCategory(brandApi.checkBrandCategory(f.getBrand(), f.getCategory()));
                api.getProductBarcode(p);
            } catch (Exception e) {
                errorSize++;
                productErrorData.setMessage(e.getMessage());
            }
            errorData.add(productErrorData);
        }
        if(errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }

        bulkAdd(forms);

//        ProductHelper.normalize(form);
//        ValidationUtil.validateForms(form);
//        ProductPojo p = ProductHelper.convert(form);
//        p.setBrandCategory(brandApi.getBrandCategoryId(form.getBrand(), form.getCategory()));
//        api.add(p);
    }
//Todo: only throwing one error brand category doesnt exists even when other erros should be shown

    public ProductData get(int id) throws ApiException {
        ProductPojo p = api.get(id);
        return ProductHelper.convert(p);
    }

    public List<ProductData> getAll() {
        return api.getAll().stream().map(ProductHelper::convert).collect(Collectors.toList());
    }

    public void update(int id, ProductUpdateForm f) throws ApiException {
        ProductHelper.normalize(f);
        ValidationUtil.validateForms(f);
        ProductPojo p = ProductHelper.convert(f);
        api.update(id, p);
    }

    @Transactional(rollbackOn = ApiException.class)
    private void bulkAdd(List<ProductForm> productForms) throws ApiException {
        for(ProductForm f: productForms){
            ProductPojo p = ProductHelper.convert(f);
            api.add(p);
        }
    }

}
