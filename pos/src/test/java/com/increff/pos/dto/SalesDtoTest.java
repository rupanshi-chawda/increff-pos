package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.SalesApi;
import com.increff.pos.helper.BrandTestHelper;
import com.increff.pos.helper.InventoryTestHelper;
import com.increff.pos.helper.OrderTestHelper;
import com.increff.pos.helper.ProductTestHelper;
import com.increff.pos.model.form.*;
import com.increff.pos.pojo.SalesPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SalesDtoTest extends AbstractUnitTest {

    @Autowired
    private SalesDto dto;

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private SalesApi api;

    @Test
    public void getAllTest() throws ApiException {
        SalesForm salesForm = new SalesForm();

        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair","qwer1234", "superSonic dryer", 32000.95);
        productFormList.add(productForm2);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        inventoryFormList.add(inventoryForm);
        InventoryForm inventoryForm2 = InventoryTestHelper.createForm("qwer1234", 17);
        inventoryFormList.add(inventoryForm2);
        inventoryDto.add(inventoryFormList);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        orderItemFormList.add(orderItemForm);
        orderDto.addItem(orderItemFormList);

        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        orderItemFormList.add(orderItemForm2);
        orderDto.addItem(orderItemFormList);

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

            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson", "hair", "A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            ProductForm productForm2 = ProductTestHelper.createForm("dyson", "hair", "qwer1234", "superSonic dryer", 32000.95);
            productFormList.add(productForm2);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();
            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            inventoryFormList.add(inventoryForm);
            InventoryForm inventoryForm2 = InventoryTestHelper.createForm("qwer1234", 17);
            inventoryFormList.add(inventoryForm2);
            inventoryDto.add(inventoryFormList);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4", 5, 40599.95);
            orderItemFormList.add(orderItemForm);
            OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234", 3, 29000.95);
            orderItemFormList.add(orderItemForm2);
            orderDto.addItem(orderItemFormList);

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
        SalesForm salesForm = new SalesForm();

        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair","qwer1234", "superSonic dryer", 32000.95);
        productFormList.add(productForm2);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        inventoryFormList.add(inventoryForm);
        InventoryForm inventoryForm2 = InventoryTestHelper.createForm("qwer1234", 17);
        inventoryFormList.add(inventoryForm2);
        inventoryDto.add(inventoryFormList);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        orderItemFormList.add(orderItemForm);
        orderDto.addItem(orderItemFormList);

        dto.createReport();
        List<SalesPojo> salesPojoList1 = dto.getAll();
        int count1 = salesPojoList1.get(0).getInvoicedOrderCount();
        int itemCount1 = salesPojoList1.get(0).getInvoicedItemsCount();
        assertEquals(1, salesPojoList1.size());
        assertEquals(1, count1);
        assertEquals(5, itemCount1);

        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        orderItemFormList.add(orderItemForm2);
        orderDto.addItem(orderItemFormList);

        dto.createReport();
        List<SalesPojo> salesPojoList2 = dto.getAll();
        int count2 = salesPojoList1.get(0).getInvoicedOrderCount();
        int itemCount2 = salesPojoList1.get(0).getInvoicedItemsCount();
        assertEquals(1, salesPojoList2.size());
        assertEquals(2, count2);
        assertEquals(13, itemCount2);

    }
}
