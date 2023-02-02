package com.increff.pos.helper;

import com.increff.pos.util.StringUtil;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class OrderHelper {

    public static OrderData convert(OrderPojo p) {
        OrderData d = new OrderData();
        d.setId(p.getId());
        d.setTime(LocalDateTime.ofInstant(p.getTime().toInstant(), ZoneOffset.ofHoursMinutes(5, 30)));
        //d.setTime(p.getTime());
        return d;
    }

    public static OrderPojo convert(OrderForm form) {
        return new OrderPojo();
    }

    public static OrderItemData convert(OrderItemPojo p, String barcode) {
        OrderItemData d = new OrderItemData();
        d.setBarcode(barcode);
        d.setId(p.getId());
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        return d;
    }

    public static OrderItemPojo convert(OrderItemForm f) {
        OrderItemPojo p = new OrderItemPojo();
        p.setSellingPrice(f.getSellingPrice());
        p.setQuantity(f.getQuantity());
        return p;
    }

    public static void normalize(OrderItemForm f) {
        f.setBarcode(StringUtil.toLowerCase(f.getBarcode()));
    }

//    public static void validate(OrderItemForm f) throws ApiException {
//        if(StringUtil.isEmpty(f.getBarcode())) {
//            throw new ApiException("Barcode cannot be empty");
//        }
//        if(f.getQuantity()<=0) {
//            throw new ApiException("Quantity cannot be empty or less than one");
//        }
//        if(f.getSellingPrice()<=0) {
//            throw new ApiException("Selling Price cannot be empty or less than one");
//        }
//    }

    public static void validateId(OrderItemPojo p, int id) throws ApiException {
        if (Objects.isNull(p)) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
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

    public static void validateBarcode(String barcode) throws ApiException {
        if(Objects.isNull(barcode)) {
            throw new ApiException("Product with given barcode does not exists");
        }
    }
}
