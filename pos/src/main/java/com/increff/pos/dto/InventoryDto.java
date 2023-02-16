package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.model.commons.InventoryItem;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.model.data.InventoryErrorData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.util.*;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service//todo add only one, i.i service
public class InventoryDto {
    
    @Autowired
    private InventoryApi api;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Autowired
    private InventoryFlowApi flowApi;

    @Autowired
    private CsvFileGenerator csvGenerator;

    public void add(List<InventoryForm> forms) throws ApiException {
        InventoryHelper.validateFormLists(forms);
        flowApi.add(forms);
    }

    public InventoryData get(String barcode) throws ApiException {
        productApi.checkProductBarcodeExistence(barcode);
        Integer id = productApi.getIdByBarcode(barcode);
        InventoryPojo p = api.get(id);
        return InventoryHelper.convert(p, barcode);
    }

    public List<InventoryData> getAll() {
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
        ValidationUtil.validateForms(f);
        productApi.checkProductBarcodeExistence(barcode);
        InventoryPojo p = InventoryHelper.convert(f);
        p.setId(productApi.getIdByBarcode(f.getBarcode()));
        api.update(p);
    }

    public List<InventoryItem> getAllItem() throws ApiException {
        List<InventoryData> inventoryDataList = getAll();
            List<InventoryItem> inventoryItemList = new ArrayList<>();
        HashMap<Pair<String, String>, Integer> map = new HashMap<>();

        for(InventoryData inventoryData : inventoryDataList)
        {
            BrandPojo brandPojo = brandApi.get(productApi.get(inventoryData.getId()).getBrandCategory());
            Pair<String, String> pair= new Pair<>(brandPojo.getBrand(), brandPojo.getCategory());
            if(map.containsKey(pair)) {
                Integer prev = map.get(pair);
                map.replace(pair, prev+inventoryData.getQuantity());
            }
            else {
                map.put(pair,inventoryData.getQuantity());
            }
        }

        for(Map.Entry<Pair<String,String>, Integer> mapElement : map.entrySet()) {
            inventoryItemList.add(InventoryHelper.convertMapToItem(mapElement));
        }
        return inventoryItemList;
    }

    public void generateCsv(HttpServletResponse response) throws ApiException {
        response.setContentType("text/csv");

        LocalDateTime lt = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm");
        String identifier = lt.format(dateTimeFormatter);

        response.addHeader("Content-Disposition", "attachment; filename=\"inventoryReport"+identifier+".csv");
        try {
            csvGenerator.writeInventoryToCsv(getAllItem(), response.getWriter());
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
