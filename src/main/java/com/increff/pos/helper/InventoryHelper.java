package com.increff.pos.helper;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.StringUtil;
import io.swagger.annotations.Api;

import java.util.Objects;

public class InventoryHelper {


    public static InventoryData convert(InventoryPojo p, String barcode) {
        InventoryData d = new InventoryData();
        d.setBarcode(barcode);
        d.setId(p.getId());
        d.setQuantity(p.getQuantity());
        return d;
    }

    public static InventoryPojo convert(InventoryForm f) {
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(f.getQuantity());
        return p;
    }

    public static void normalize(InventoryForm f) {
        f.setBarcode(StringUtil.toLowerCase(f.getBarcode()));
    }

    public static void validate(InventoryForm f) throws ApiException {
        if(StringUtil.isEmpty(f.getBarcode())) {
            throw new ApiException("Barcode cannot be empty");
        }
        if(f.getQuantity()<=0) {
            throw new ApiException("Quantity cannot be empty or less than one");
        }
    }

    public static void validateId(InventoryPojo p, int id) throws ApiException {
        if (Objects.isNull(p)) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
    }
}
