package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.OrderHelper;
import com.increff.pos.util.InvoiceGenerator;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
//import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Service
public class OrderDto {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private InvoiceGenerator invoiceGenerator;

    //TODO general clean up
    //1. remove system.out
    //2. remove console.out
    //3. log statements(info) and comments

    public List<OrderData> getAllOrder() {
        return orderApi.getAllOrder().stream().map(OrderHelper::convert).collect(Collectors.toList());
    }

    public void addItem(List<OrderItemForm> forms) throws ApiException {
        // Validating every order item before adding it.

        checkDuplicateItems(forms);
        validateItems(forms);

        // Place order
        OrderPojo op = new OrderPojo();
        orderApi.addOrder(op);

        // Adding order item to table
        for(OrderItemForm f : forms) {
            OrderItemPojo p = OrderHelper.convert(f);
            //Reduce inventory
            reduceInventory(f.getBarcode(), f.getQuantity());
            int pid = productApi.getIdByBarcode(f.getBarcode());
            orderApi.addItem(p, pid, op.getId());
        }
    }

    public List<OrderItemData> getByOrderId(int orderId) {
        List<OrderItemPojo> list = orderApi.getByOrderId(orderId);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for(OrderItemPojo b : list) {
            String barcode = productApi.getBarcodeById(b.getProductId());
            list2.add(OrderHelper.convert(b, barcode));
        }
        return list2;
    }

    public ResponseEntity<byte[]> getPDF(int id) throws ApiException {
        InvoiceForm invoiceForm = invoiceGenerator.generateInvoiceForOrder(id);
        return orderApi.getPDF(invoiceForm);
    }

    private void reduceInventory(String barcode, int quantity) throws ApiException {
        int id = productApi.getIdByBarcode(barcode);
        InventoryPojo p = inventoryApi.getByInventoryId(id);
        int newQuantity = p.getQuantity() - quantity;
        p.setQuantity(newQuantity);
        inventoryApi.update(p);
    }

    private void validateItems(List<OrderItemForm> forms) throws ApiException {
        for(OrderItemForm f : forms) {
            productApi.checkProductBarcode(f.getBarcode());

            int id = productApi.getIdByBarcode(f.getBarcode());
            OrderHelper.validateId(id);

            inventoryApi.get(id);

            int quantity = inventoryApi.getQuantityById(id);
            OrderHelper.validateInventory(f, quantity);
        }
    }

    private void checkDuplicateItems(List<OrderItemForm> forms) throws ApiException {
        Set<String> set = new HashSet<>();
        for(OrderItemForm f : forms) {
            ValidationUtil.validateForms(f);
            OrderHelper.normalize(f);
            if(set.contains(f.getBarcode())) {
                throw new ApiException("Duplicate Barcode Detected");
            }
            set.add(f.getBarcode());
        }
    }
}
