package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.*;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.*;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SalesReportDtoTest extends AbstractUnitTest {

    @Autowired
    private SalesReportDto dto;

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private OrderFlowApi orderFlowApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderFlowApi flowApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Test
    public void getFilterTest() throws ApiException {
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

        LocalDate date = LocalDate.now();
        SalesReportForm salesReportForm = SalesReportTestHelper.createForm(date.toString(), date.toString(), "dyson","hair");
        List<SalesReportData> list = dto.getFilterAll(salesReportForm);

        assertEquals(1, list.size());
    }

    @Test
    public void testReportCsv() throws ApiException {
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

        MockHttpServletResponse response = new MockHttpServletResponse();
        LocalDate date = LocalDate.now();
        SalesReportForm salesReportForm = SalesReportTestHelper.createForm(date.toString(), date.toString(), "dyson","hair");
        dto.generateCsv(salesReportForm, response);
        assertEquals("text/csv", response.getContentType());
    }

    @Test(expected = ApiException.class)
    public void getIllegalFilterTest() throws ApiException {
        try {
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
            SalesReportForm salesReportForm = SalesReportTestHelper.createForm(sdate.toString(), edate.toString(), "all", "all");
            List<SalesReportData> list = dto.getFilterAll(salesReportForm);
        }
        catch(ApiException e)
        {
            String exception = "End date must not be before Start date";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

}
