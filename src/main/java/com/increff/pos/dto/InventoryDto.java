package com.increff.pos.dto;

import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
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
    private final InventoryService service;

    @Autowired
    private final ProductService productService;

    public InventoryDto(InventoryService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }


    public void add(InventoryForm form) throws ApiException {
        InventoryHelper.normalize(form);
        InventoryHelper.validate(form);
        InventoryPojo p = InventoryHelper.convert(form);
        p.setId(productService.getIdByBarcode(form.getBarcode()));
        service.add(p);
    }

    public InventoryData get(int id) throws ApiException {
        InventoryPojo p = service.get(id);
        String barcode = productService.getBarcodeById(id);
        return InventoryHelper.convert(p, barcode);
    }

    public List<InventoryData> getAll() {
        //TODO: apply java stream method
        List<InventoryPojo> list = service.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo b : list) {
            String barcode = productService.getBarcodeById(b.getId());
            list2.add(InventoryHelper.convert(b, barcode));
        }
        return list2;
    }

    public void update(int id, InventoryForm f) throws ApiException {
        InventoryHelper.normalize(f);
        InventoryHelper.validate(f);
        InventoryPojo p = InventoryHelper.convert(f);
        service.update(id, p);
    }
}
