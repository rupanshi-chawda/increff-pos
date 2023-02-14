package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.model.data.InventoryErrorData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
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
public class InventoryFlowApi {

    @Autowired
    private InventoryApi api;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<InventoryForm> forms, List<InventoryErrorData> errorData) throws ApiException {
        Integer errorSize = 0;
        Integer i=0;

        for(InventoryForm f: forms)
        {
            InventoryErrorData inventoryErrorData = ConvertUtil.convert(f, InventoryErrorData.class);
            inventoryErrorData.setMessage("");
            try
            {
                productApi.checkProductBarcode(f.getBarcode());
            }
            catch (ApiException e) {
                errorSize++;
                inventoryErrorData.setMessage(e.getMessage());
            }
            errorData.get(i).setMessage(inventoryErrorData.getMessage());
            i++;
        }
        if(errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }
        else {
            for(InventoryForm f: forms){
                InventoryPojo p = InventoryHelper.convert(f);
                p.setId(productApi.getIdByBarcode(f.getBarcode()));
                api.add(p);
            }
        }
    }
}
