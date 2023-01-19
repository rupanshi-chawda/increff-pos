package com.increff.pos.dto;

import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class InventoryDto {
    
    @Autowired
    private InventoryApi api;

    @Autowired
    private ProductApi productApi;


    public void add(InventoryForm form) throws ApiException {
        InventoryHelper.normalize(form);
        InventoryHelper.validate(form);
        productApi.checkProductBarcode(form.getBarcode());
        InventoryPojo p = InventoryHelper.convert(form);
        p.setId(productApi.getIdByBarcode(form.getBarcode()));
        api.add(p);
    }

    public InventoryData get(String barcode) throws ApiException {
        productApi.checkProductBarcode(barcode);
        int id = productApi.getIdByBarcode(barcode);
        InventoryPojo p = api.get(id);
        return InventoryHelper.convert(p, barcode);
    }

    public List<InventoryData> getAll() {
        //TODO: apply java stream method
        List<InventoryPojo> list = api.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo b : list) {
            String barcode = productApi.getBarcodeById(b.getId());
            list2.add(InventoryHelper.convert(b, barcode));
        }
        return list2;
    }

    public void update(String barcode, InventoryForm f) throws ApiException {
        InventoryHelper.normalize(f);
        InventoryHelper.validate(f);
        productApi.checkProductBarcode(barcode);
        InventoryPojo p = InventoryHelper.convert(f);
        p.setId(productApi.getIdByBarcode(f.getBarcode()));
        api.update(p);
    }
}
