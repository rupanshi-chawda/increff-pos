package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.*;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.*;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto dto;

    @Autowired
    private OrderApi orderApi;

    @Value("${invoice.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Autowired
    private OrderFlowApi flowApi;

    @Test
    public void addOrderTest() throws ApiException {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        inventoryPojo.setId(productApi.getAll().get(0).getId());
        inventoryApi.add(inventoryPojo);

        List<InventoryPojo> list1 = inventoryApi.getAll();

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        orderItemFormList.add(orderItemForm);
        dto.addItem(orderItemFormList);

        String expectedBarcode = "a1b2c3d4";
        Integer expectedQty = 5;
        Double expectedSp = 40599.95;

        List<OrderData> list = orderApi.getAllOrder().stream().map(OrderHelper::convert).collect(Collectors.toList());
        List<OrderItemPojo> orderItemPojoList = orderApi.getItemsByOrderId(list.get(0).getId());
        List<OrderItemData> orderItemDataList = new ArrayList<OrderItemData>();
        for(OrderItemPojo b : orderItemPojoList) {
            String barcode = productApi.getBarcodeById(b.getProductId());
            orderItemDataList.add(OrderHelper.convert(b, barcode));
        }

        assertEquals(expectedBarcode, orderItemDataList.get(0).getBarcode());
        assertEquals(expectedQty, orderItemDataList.get(0).getQuantity());
        assertEquals(expectedSp, orderItemDataList.get(0).getSellingPrice());
    }

    @Test(expected = ApiException.class)
    public void addDuplicateOrderItem() throws ApiException {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
            inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
            inventoryApi.add(inventoryPojo);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4", 5, 40599.95);
            orderItemFormList.add(orderItemForm);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException e)
        {
            String exception = "Duplicate Barcode Detected";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addIllegalOrderItem() throws ApiException {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4", 5, 40599.95);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException e)
        {
            String exception = "Inventory for given barcode doesn't exists";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4", 5, 40599.95);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException e)
        {
            String exception = "Product with given barcode does not exists";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
            inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
            inventoryApi.add(inventoryPojo);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4", 50, 40599.95);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException e)
        {
            String exception = "Product Quantity is more than available Inventory";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addEmptyOrderItem() throws ApiException {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
            inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
            inventoryApi.add(inventoryPojo);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm(" ", 5, 40599.95);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException e)
        {
            String exception1 = "[barcode must 8 character long, barcode must not be blank]";
            String exception2 = "[barcode must not be blank, barcode must 8 character long]";
            assertThat(e.getMessage(), anyOf(containsString(exception1),containsString(exception2)));
            throw e;
        }
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
            inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
            inventoryApi.add(inventoryPojo);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4", -5, -40599.95);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException e)
        {
            String exception = "[quantity must be atleast 1, sellingPrice must be atleast 0]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test
    public void getOrderTest() throws ApiException {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair","qwer1234", "supersonic dryer", 32000.95);
        ProductPojo py = ProductHelper.convert(productForm2);
        py.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm2.getBrand(), productForm2.getCategory()));
        productApi.add(py);

        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
        inventoryApi.add(inventoryPojo);

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("qwer1234", 15);
        InventoryPojo inventoryPojo1 = InventoryHelper.convert(inventoryForm1);
        inventoryPojo1.setId(productApi.getIdByBarcode(inventoryForm1.getBarcode()));
        inventoryApi.add(inventoryPojo1);


        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        OrderItemPojo p = OrderHelper.convert(orderItemForm);
        flowApi.reduceInventory(orderItemForm.getBarcode(), orderItemForm.getQuantity());
        Integer pid = productApi.getIdByBarcode(orderItemForm.getBarcode());
        OrderPojo op = new OrderPojo();
        orderApi.addOrder(op);
        orderApi.addItem(p, pid, op.getId());

        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        OrderItemPojo p2 = OrderHelper.convert(orderItemForm2);
        flowApi.reduceInventory(orderItemForm2.getBarcode(), orderItemForm2.getQuantity());
        Integer pid2 = productApi.getIdByBarcode(orderItemForm2.getBarcode());
        OrderPojo op2 = new OrderPojo();
        orderApi.addOrder(op2);
        orderApi.addItem(p2, pid2, op2.getId());


        List<OrderData> list = dto.getAllOrder();
        List<OrderItemData> itemList1 = dto.getByOrderId(list.get(0).getId());
        List<OrderItemData> itemList2 = dto.getByOrderId(list.get(1).getId());
        assertEquals(2, list.size());
        assertEquals(1, itemList1.size());
        assertEquals(1, itemList2.size());
    }

    @Test
    public void invoiceTest() throws Exception {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
        inventoryApi.add(inventoryPojo);

        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        OrderItemPojo p = OrderHelper.convert(orderItemForm);
        flowApi.reduceInventory(orderItemForm.getBarcode(), orderItemForm.getQuantity());
        Integer pid = productApi.getIdByBarcode(orderItemForm.getBarcode());
        OrderPojo op = new OrderPojo();
        orderApi.addOrder(op);
        orderApi.addItem(p, pid, op.getId());


        List<OrderData> list = orderApi.getAllOrder().stream().map(OrderHelper::convert).collect(Collectors.toList());
        InvoiceForm invoiceForm = new InvoiceForm();

        restTemplate = mock(RestTemplate.class);
        when(restTemplate.postForObject(url, invoiceForm, String.class)).thenReturn(String.valueOf(HttpStatus.OK));

        ResponseEntity<byte[]> response = dto.getPDF(list.get(0).getId());
        assertNotEquals(null, response);
    }

}
