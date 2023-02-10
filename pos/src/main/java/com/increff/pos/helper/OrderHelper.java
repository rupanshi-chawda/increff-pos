package com.increff.pos.helper;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
//import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class OrderHelper {

    public static OrderData convert(OrderPojo p) {
        return ConvertUtil.convert(p, OrderData.class);
    }

    public static OrderItemData convert(OrderItemPojo p, String barcode) {
        OrderItemData d =  ConvertUtil.convert(p, OrderItemData.class);;
        d.setBarcode(barcode);
        return d;
    }

    public static OrderItemPojo convert(OrderItemForm f) {
        return ConvertUtil.convert(f, OrderItemPojo.class);
    }

    public static void normalize(OrderItemForm f) {
        f.setBarcode(StringUtil.toLowerCase(f.getBarcode()));
    }

    public static void validateId(int id) throws ApiException {
        if (id<1) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
    }

    public static void validateInventory(OrderItemForm f, int quantity) throws ApiException {
        if (f.getQuantity() > quantity) {
            throw new ApiException("Product Quantity is more than available Inventory");
        }
    }

}
