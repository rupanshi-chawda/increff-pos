package com.increff.pos.dto;

import com.increff.pos.helper.OrderItemHelper;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class OrderItemDto {

    @Autowired
    private final OrderItemService service;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final InventoryService inventoryService;

    @Autowired
    private final OrderService orderService;

    public OrderItemDto(OrderItemService service, ProductService productService, InventoryService inventoryService, OrderService orderService) {
        this.service = service;
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    public void add(List<OrderItemForm> forms) throws ApiException {

        // Validating every order item before adding it.
        for(OrderItemForm f : forms) {
            OrderItemHelper.normalize(f);
            OrderItemHelper.validate(f);

            int id = productService.getIdByBarcode(f.getBarcode());
            OrderItemHelper.validateId(id);

            int quantity = inventoryService.getQuantityById(id);
            OrderItemHelper.validateInventory(f, quantity);
        }

        // Place order
        OrderPojo op = new OrderPojo();
        orderService.add(op);

        // Adding order item to table
        for(OrderItemForm f : forms) {
            OrderItemPojo p = OrderItemHelper.convert(f);
            //Reduce inventory
            reduceInventory(f.getBarcode(), f.getQuantity());
            service.add(p, f.getBarcode(), op.getId());
        }
    }

    public OrderItemData get(int id) throws ApiException {
        OrderItemPojo p = service.get(id);
        String barcode = productService.getBarcodeById(p.getProductId());
        return OrderItemHelper.convert(p, barcode);
    }

    public List<OrderItemData> getAll() {
        //TODO: apply java stream method
        List<OrderItemPojo> list = service.getAll();
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for(OrderItemPojo b : list) {
            String barcode = productService.getBarcodeById(b.getProductId());
            list2.add(OrderItemHelper.convert(b, barcode));
        }
        return list2;
    }

    public List<OrderItemData> getByOrderId(int orderid) throws ApiException {
        List<OrderItemPojo> list = service.getByOrderId(orderid);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for(OrderItemPojo b : list) {
            String barcode = productService.getBarcodeById(b.getProductId());
            list2.add(OrderItemHelper.convert(b, barcode));
        }
        return list2;
    }

    private void reduceInventory(String barcode, int quantity) throws ApiException {
        int id = productService.getIdByBarcode(barcode);
        InventoryPojo p = inventoryService.getInventoryId(id);
        int newQuantity = p.getQuantity() - quantity;
        p.setQuantity(newQuantity);
        inventoryService.update(id, p);
    }

}
