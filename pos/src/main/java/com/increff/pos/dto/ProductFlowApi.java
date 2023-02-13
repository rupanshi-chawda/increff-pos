package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.data.ProductErrorData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Service
public class ProductFlowApi {

    @Autowired
    private ProductApi api;

    @Autowired
    private BrandApi brandApi;

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<ProductForm> forms, List<ProductErrorData> errorData) throws ApiException {
        int errorSize = 0;
        int i=0;

        for(ProductForm f: forms)
        {
            ProductErrorData productErrorData = ConvertUtil.convert(f, ProductErrorData.class);
            productErrorData.setMessage("");
            try
            {
                ProductPojo p = ProductHelper.convert(f);
                p.setBrandCategory(brandApi.checkBrandCategory(f.getBrand(), f.getCategory()));
                api.getProductBarcode(p);
            }
            catch (ApiException e) {
                errorSize++;
                productErrorData.setMessage(e.getMessage());
            }
            errorData.get(i).setMessage(productErrorData.getMessage());
            i++;
        }
        if(errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }
        else {
            for(ProductForm f: forms){
                ProductPojo p = ProductHelper.convert(f);
                p.setBrandCategory(brandApi.checkBrandCategory(f.getBrand(), f.getCategory()));
                api.add(p);
            }
        }
    }

}
