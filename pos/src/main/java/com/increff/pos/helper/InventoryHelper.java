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


    public static InventoryData convert(InventoryPojo p, String barcode) {
        InventoryData d = ConvertUtil.convert(p, InventoryData.class);
        d.setBarcode(barcode);
        return d;
    }

    public static InventoryPojo convert(InventoryForm f) {
        return ConvertUtil.convert(f, InventoryPojo.class);
    }

    public static void normalize(InventoryForm f) {
        f.setBarcode(StringUtil.toLowerCase(f.getBarcode()));
    }


    public static void validateId(InventoryPojo p, int id) throws ApiException {
        if (Objects.isNull(p)) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
    }

    public static void validateInventoryId(InventoryPojo p) throws ApiException {
        if (Objects.isNull(p)) {
            throw new ApiException("Inventory for given barcode doesn't exists");
        }
    }

    public static InventoryItem convertMapToItem(Map.Entry<Pair<String,String>,Integer> mapElement) {
        Pair<String, String> p = mapElement.getKey();
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setBrand(p.getKey());
        inventoryItem.setCategory(p.getValue());
        inventoryItem.setQuantity(mapElement.getValue());
        return inventoryItem;
    }
}
