package com.increff.pos.dto;

import com.increff.pos.model.data.ProductErrorData;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class ProductDto {

    @Autowired
    private ProductApi api;

    @Autowired
    private ProductFlow flow;

    @Autowired
    private BrandApi brandApi;

    public void add(List<ProductForm> forms) throws ApiException {
        List<ProductErrorData> errorData = new ArrayList<>();
        errorData.clear();
        int errorSize = 0;

        for(ProductForm f: forms)
        {
            ProductErrorData productErrorData = ConvertUtil.convert(f, ProductErrorData.class);
            productErrorData.setMessage("");
            try
            {
                ProductHelper.normalize(f);
                ValidationUtil.validateForms(f);
            }
            catch (ApiException e) {
                errorSize++;
                productErrorData.setMessage(e.getMessage());
            }
            errorData.add(productErrorData);
        }
        if(errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }

        flow.add(forms, errorData);
    }

    public ProductData get(int id) throws ApiException {
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

    public void update(int id, ProductUpdateForm f) throws ApiException {
        ProductHelper.normalize(f);
        ValidationUtil.validateForms(f);
        ProductPojo p = ProductHelper.convert(f);
        api.update(id, p);
    }

}