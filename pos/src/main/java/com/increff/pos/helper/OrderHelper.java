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

    public static OrderData convert(OrderPojo pojo) {
        return ConvertUtil.convert(pojo, OrderData.class);
    }

    public static OrderItemData convert(OrderItemPojo pojo, String barcode) {
        OrderItemData data =  ConvertUtil.convert(pojo, OrderItemData.class);;
        data.setBarcode(barcode);
        return data;
    }

    public static OrderItemPojo convert(OrderItemForm form) {
        return ConvertUtil.convert(form, OrderItemPojo.class);
    }

    public static void normalize(OrderItemForm form) {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
    }

    public static void validateId(Integer id) throws ApiException {
        if (id<1) {
            throw new ApiException("Product with given ID does not exists, id: " + id);
        }
    }

    public static void validateInventory(OrderItemForm form, Integer quantity) throws ApiException {
        if (form.getQuantity() > quantity) {
            throw new ApiException("Product Quantity is more than available Inventory");
        }
    }

    public static void validateSellingPrice(OrderItemForm form, Double mrp) throws ApiException {
        if(form.getSellingPrice() > mrp) {
            throw new ApiException("Selling Price cannot be greater than MRP");
        }
    }
}
