package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandErrorData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
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

@Component
@Service
public class BrandFlowApi {

    @Autowired
    private BrandApi api;

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<BrandForm> brandForms, List<BrandErrorData> errorData) throws ApiException {
        int errorSize = 0;
        int i=0;

        for(BrandForm f: brandForms)
        {
            BrandErrorData brandErrorData = ConvertUtil.convert(f, BrandErrorData.class);
            brandErrorData.setMessage("");
            try
            {
                BrandPojo b = BrandHelper.convert(f);
                api.getCheckBrandCategory(b);
            }
            catch (ApiException e) {
                errorSize++;
                brandErrorData.setMessage(e.getMessage());
                System.out.println(e.getMessage());
            }
            errorData.get(i).setMessage(brandErrorData.getMessage());
            i++;
        }
        if (errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }
        else{
            for (BrandForm f: brandForms){
                BrandPojo b = BrandHelper.convert(f);
                api.add(b);
            }
        }
    }

}
