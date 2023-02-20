package com.increff.pos.helper;

import com.increff.pos.model.commons.InventoryItem;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.InventoryErrorData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoryHelper {


    public static InventoryData convert(InventoryPojo pojo, String barcode, Double mrp) {
        InventoryData data = ConvertUtil.convert(pojo, InventoryData.class);
        data.setBarcode(barcode);
        data.setMrp(mrp);
        return data;
    }

    public static InventoryPojo convert(InventoryForm form) {
        return ConvertUtil.convert(form, InventoryPojo.class);
    }

    public static void normalize(InventoryForm form) {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
    }


    public static void validateId(InventoryPojo pojo, Integer id) throws ApiException {
        if (Objects.isNull(pojo)) {
            throw new ApiException("Product with given ID does not exists, id: " + id);
        }
    }

    public static void validateInventoryId(InventoryPojo pojo) throws ApiException {
        if (Objects.isNull(pojo)) {
            throw new ApiException("Inventory for given barcode doesn't exists");
        }
    }

    public static InventoryItem convertMapToItem(Map.Entry<Pair<String,String>,Integer> mapElement) {
        Pair<String, String> pojo = mapElement.getKey();
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setBrand(pojo.getKey());
        inventoryItem.setCategory(pojo.getValue());
        inventoryItem.setQuantity(mapElement.getValue());
        return inventoryItem;
    }

    public static void validateFormLists(List<InventoryForm> forms) throws ApiException {
        List<InventoryErrorData> errorData = new ArrayList<>();
        Integer errorSize = 0;

        for(InventoryForm f: forms)
        {
            InventoryErrorData inventoryErrorData = ConvertUtil.convert(f, InventoryErrorData.class);
            inventoryErrorData.setMessage("");
            try
            {
                InventoryHelper.normalize(f);
                ValidationUtil.validateForms(f);
            }
            catch (ApiException e) {
                errorSize++;
                inventoryErrorData.setMessage(e.getMessage());
            }
            errorData.add(inventoryErrorData);
        }
        if(errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }
    }
}
