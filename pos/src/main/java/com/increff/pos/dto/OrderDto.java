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


@Service
public class OrderDto {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderFlowApi flowApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InvoiceGenerator invoiceGenerator;

    public void addItem(List<OrderItemForm> forms) throws ApiException {
        checkOrderNotEmpty(forms);
        checkDuplicateItems(forms);
        flowApi.addItem(forms);
    }

    public List<OrderData> getAllOrder() {
        return orderApi.getAllOrder().stream().map(OrderHelper::convert).collect(Collectors.toList());
    }

    public List<OrderItemData> getByOrderId(Integer orderId) {
        List<OrderItemPojo> list = orderApi.getItemsByOrderId(orderId);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for(OrderItemPojo b : list) {
            String barcode = productApi.getBarcodeById(b.getProductId());
            list2.add(OrderHelper.convert(b, barcode));
        }
        return list2;
    }

    public ResponseEntity<byte[]> getPDF(Integer id) throws ApiException {
        InvoiceForm invoiceForm = invoiceGenerator.generateInvoiceForOrder(id);
        return orderApi.getPDF(invoiceForm);
    }

    private void checkDuplicateItems(List<OrderItemForm> forms) throws ApiException {
        Set<String> set = new HashSet<>();
        for(OrderItemForm f : forms) {
            OrderHelper.normalize(f);
            ValidationUtil.validateForms(f);
            if(set.contains(f.getBarcode())) {
                throw new ApiException("Duplicate Barcode Detected");
            }
            set.add(f.getBarcode());
        }
    }

    private void checkOrderNotEmpty(List<OrderItemForm> forms) throws ApiException {
        if(forms.size()<1)
            throw new ApiException("Cannot Place Empty Order");
    }
}
