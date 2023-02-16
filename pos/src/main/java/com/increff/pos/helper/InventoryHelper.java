package com.increff.pos.helper;

import com.increff.pos.model.commons.InventoryItem;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ApiException;
import javafx.util.Pair;

import java.util.Map;
import java.util.Objects;

public class InventoryHelper {


    public static InventoryData convert(InventoryPojo pojo, String barcode) {
        InventoryData data = ConvertUtil.convert(pojo, InventoryData.class);
        data.setBarcode(barcode);
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
}
