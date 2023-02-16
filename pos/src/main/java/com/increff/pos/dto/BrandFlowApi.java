package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandErrorData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandFlowApi {

    @Autowired
    private BrandApi api;

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<BrandForm> brandForms) throws ApiException {
        for (BrandForm f: brandForms){
            BrandPojo b = BrandHelper.convert(f);
            api.checkBrandCategoryExist(b);
            api.add(b);
        }
    }

}
