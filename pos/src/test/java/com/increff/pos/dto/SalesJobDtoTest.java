package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.*;
import com.increff.pos.helper.*;
import com.increff.pos.model.form.*;
import com.increff.pos.pojo.*;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SalesJobDtoTest extends AbstractUnitTest {

    @Autowired
    private SalesJobDto dto;

    @Autowired
    private OrderFlowApi orderFlowApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Autowired
    private SalesJobApi api;

    @Test
    public void getAllTest() throws ApiException {
        SalesForm salesForm = new SalesForm();

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
        orderFlowApi.reduceInventory(orderItemForm.getBarcode(), orderItemForm.getQuantity());
        Integer pid = productApi.getIdByBarcode(orderItemForm.getBarcode());
        OrderPojo op = new OrderPojo();
        orderApi.addOrder(op);
        orderApi.addItem(p, pid, op.getId());

        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        OrderItemPojo p2 = OrderHelper.convert(orderItemForm2);
        orderFlowApi.reduceInventory(orderItemForm2.getBarcode(), orderItemForm2.getQuantity());
        Integer pid2 = productApi.getIdByBarcode(orderItemForm2.getBarcode());
        OrderPojo op2 = new OrderPojo();
        orderApi.addOrder(op2);
        orderApi.addItem(p2, pid2, op2.getId());

        dto.createReport();
        List<SalesPojo> salesPojoList2 = dto.getAll();
        assertEquals(1, salesPojoList2.size());

        LocalDate date = LocalDate.now();
        salesForm.setStartDate(date.toString());
        salesForm.setEndDate(date.toString());

        List<SalesPojo> list = dto.getAllBetweenDates(salesForm);
        assertEquals(1,list.size());
    }

    @Test(expected = ApiException.class)
    public void testIllegalDates() throws ApiException {
        try
        {
            SalesForm salesForm = new SalesForm();

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
            orderFlowApi.reduceInventory(orderItemForm.getBarcode(), orderItemForm.getQuantity());
            Integer pid = productApi.getIdByBarcode(orderItemForm.getBarcode());
            OrderPojo op = new OrderPojo();
            orderApi.addOrder(op);
            orderApi.addItem(p, pid, op.getId());

            OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
            OrderItemPojo p2 = OrderHelper.convert(orderItemForm2);
            orderFlowApi.reduceInventory(orderItemForm2.getBarcode(), orderItemForm2.getQuantity());
            Integer pid2 = productApi.getIdByBarcode(orderItemForm2.getBarcode());
            OrderPojo op2 = new OrderPojo();
            orderApi.addOrder(op2);
            orderApi.addItem(p2, pid2, op2.getId());

            LocalDate sdate = LocalDate.now();
            LocalDate edate = LocalDate.now().minusDays(1);
            salesForm.setStartDate(sdate.toString());
            salesForm.setEndDate(edate.toString());

            List<SalesPojo> list = dto.getAllBetweenDates(salesForm);
        }
        catch(ApiException e)
        {
            String exception = "End date must not be before Start date";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCreateReport() throws ApiException {

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
        orderFlowApi.reduceInventory(orderItemForm.getBarcode(), orderItemForm.getQuantity());
        Integer pid = productApi.getIdByBarcode(orderItemForm.getBarcode());
        OrderPojo op = new OrderPojo();
        orderApi.addOrder(op);
        orderApi.addItem(p, pid, op.getId());

        dto.createReport();
        List<SalesPojo> salesPojoList1 = api.getAll();
        Integer count1 = salesPojoList1.get(0).getInvoicedOrderCount();
        Integer itemCount1 = salesPojoList1.get(0).getInvoicedItemsCount();
        assertEquals(1, salesPojoList1.size());
        assertEquals(1, (int) count1);
        assertEquals(5, (int) itemCount1);

        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        OrderItemPojo p2 = OrderHelper.convert(orderItemForm2);
        orderFlowApi.reduceInventory(orderItemForm2.getBarcode(), orderItemForm2.getQuantity());
        Integer pid2 = productApi.getIdByBarcode(orderItemForm2.getBarcode());
        OrderPojo op2 = new OrderPojo();
        orderApi.addOrder(op2);
        orderApi.addItem(p2, pid2, op2.getId());

        dto.createReport();
        List<SalesPojo> salesPojoList2 = api.getAll();
        Integer count2 = salesPojoList1.get(0).getInvoicedOrderCount();
        Integer itemCount2 = salesPojoList1.get(0).getInvoicedItemsCount();
        assertEquals(1, salesPojoList2.size());
        assertEquals(2,(int) count2);
        assertEquals(8, (int)itemCount2);

    }
}
