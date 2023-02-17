package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.OrderHelper;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderFlowApi {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    public void addItem(List<OrderItemForm> forms) throws ApiException {
        validateItems(forms);

        OrderPojo op = new OrderPojo();
        orderApi.addOrder(op);

        // Adding order item
        for(OrderItemForm f : forms) {
            OrderItemPojo p = OrderHelper.convert(f);
            //Reduce inventory
            reduceInventory(f.getBarcode(), f.getQuantity());
            Integer pid = productApi.getIdByBarcode(f.getBarcode());
            orderApi.addItem(p, pid, op.getId());
        }
    }

    private void validateItems(List<OrderItemForm> forms) throws ApiException {
        for(OrderItemForm f : forms) {
            productApi.checkProductBarcodeExistence(f.getBarcode());

            Integer id = productApi.getIdByBarcode(f.getBarcode());
            OrderHelper.validateId(id);

            inventoryApi.get(id);

            Integer quantity = inventoryApi.getQuantityById(id);
            OrderHelper.validateInventory(f, quantity);

            Double mrp = productApi.get(id).getMrp();
            OrderHelper.validateSellingPrice(f, mrp);
        }
    }

    protected void reduceInventory(String barcode, Integer quantity) throws ApiException {
        Integer id = productApi.getIdByBarcode(barcode);
        InventoryPojo p = inventoryApi.getByInventoryId(id);
        Integer newQuantity = p.getQuantity() - quantity;
        p.setQuantity(newQuantity);
        inventoryApi.update(p);
    }
}
