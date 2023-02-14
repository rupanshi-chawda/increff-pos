package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.*;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.*;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SalesReportDtoTest extends AbstractUnitTest {

    @Autowired
    private SalesReportDto dto;

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    @Test
    public void getAllTest() throws ApiException {
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
        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        orderItemFormList.add(orderItemForm2);
        orderDto.addItem(orderItemFormList);

        List<SalesReportData> list = dto.getAll();

        assertEquals(1, list.size());
    }

    @Test
    public void getFilterTest() throws ApiException {
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

        List<ProductData> productData = productDto.getAll();
        System.out.println(productData.get(0).getId());
        System.out.println(productData.get(1).getId());

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        inventoryFormList.add(inventoryForm);
        InventoryForm inventoryForm2 = InventoryTestHelper.createForm("qwer1234", 17);
        inventoryFormList.add(inventoryForm2);
        inventoryDto.add(inventoryFormList);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        orderItemFormList.add(orderItemForm);
        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        orderItemFormList.add(orderItemForm2);
        orderDto.addItem(orderItemFormList);

        LocalDate date = LocalDate.now();
        SalesReportForm salesReportForm = SalesReportTestHelper.createForm(date.toString(), date.toString(), "dyson","hair");
        List<SalesReportData> list = dto.getFilterAll(salesReportForm);

        assertEquals(1, list.size());
    }

    @Test
    public void testReportCsv() throws ApiException {
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
        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        orderItemFormList.add(orderItemForm2);
        orderDto.addItem(orderItemFormList);

        LocalDate date = LocalDate.now();
        SalesReportForm salesReportForm = SalesReportTestHelper.createForm(date.toString(), date.toString(), "all","all");
        List<SalesReportData> list = dto.getFilterAll(salesReportForm);
        assertEquals(1, list.size());

        MockHttpServletResponse response = new MockHttpServletResponse();
        dto.generateCsv(response);
        assertEquals("text/csv", response.getContentType());
    }

    @Test(expected = ApiException.class)
    public void getIllegalFilterTest() throws ApiException {
        try {
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
