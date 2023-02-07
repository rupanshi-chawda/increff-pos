package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.OrderApi;
import com.increff.pos.helper.BrandTestHelper;
import com.increff.pos.helper.InventoryTestHelper;
import com.increff.pos.helper.OrderTestHelper;
import com.increff.pos.helper.ProductTestHelper;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.*;
import com.increff.pos.util.ApiException;
import io.swagger.annotations.Api;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.runner.Request.method;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static springfox.documentation.builders.PathSelectors.any;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto dto;

    @Value("${invoice.url}")
    private String url;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    private MockRestServiceServer mockServer;

    @Test
    public void addOrderTest() throws JsonProcessingException, ApiException {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        inventoryFormList.add(inventoryForm);
        inventoryDto.add(inventoryFormList);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        orderItemFormList.add(orderItemForm);
        dto.addItem(orderItemFormList);

        String expectedBarcode = "a1b2c3d4";
        int expectedQty = 5;
        Double expectedSp = 40599.95;

        List<OrderData> list = dto.getAllOrder();
        List<OrderItemData> orderItemDataList = dto.getByOrderId(list.get(0).getId());

        assertEquals(expectedBarcode, orderItemDataList.get(0).getBarcode());
        assertEquals(expectedQty, orderItemDataList.get(0).getQuantity());
        assertEquals(expectedSp, orderItemDataList.get(0).getSellingPrice());
    }

    @Test(expected = ApiException.class)
    public void addDuplicateOrderItem() throws JsonProcessingException, ApiException {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson", "hair", " A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();
            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            inventoryFormList.add(inventoryForm);
            inventoryDto.add(inventoryFormList);

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
    public void addIllegalOrderItem() throws JsonProcessingException, ApiException {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson", "hair", " A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

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
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

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
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson", "hair", " A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();
            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            inventoryFormList.add(inventoryForm);
            inventoryDto.add(inventoryFormList);

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

    @Test(expected = ConstraintViolationException.class)
    public void addEmptyOrderItem() throws JsonProcessingException, ApiException {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson", "hair", " A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();
            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            inventoryFormList.add(inventoryForm);
            inventoryDto.add(inventoryFormList);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm(" ", 5, 40599.95);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException | ConstraintViolationException e)
        {
            String exception = "barcode: must 8 character long, barcode: must not be blank";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson", "hair", " A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();
            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            inventoryFormList.add(inventoryForm);
            inventoryDto.add(inventoryFormList);

            List<OrderItemForm> orderItemFormList = new ArrayList<>();
            OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4", -5, -40599.95);
            orderItemFormList.add(orderItemForm);
            dto.addItem(orderItemFormList);
        }
        catch(ApiException | ConstraintViolationException e)
        {
            String exception = "quantity: must be atleast 1, sellingPrice: must be atleast 1";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test
    public void getOrderTest() throws JsonProcessingException, ApiException {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);


        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair"," qwer1234", "superSonic dryer", 32000.95);
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
        dto.addItem(orderItemFormList);

        List<OrderItemForm> orderItemFormList2 = new ArrayList<>();
        OrderItemForm orderItemForm2 = OrderTestHelper.createForm("qwer1234",3,29000.95);
        orderItemFormList2.add(orderItemForm2);
        dto.addItem(orderItemFormList2);

        List<OrderData> list = dto.getAllOrder();
        List<OrderItemData> itemList1 = dto.getByOrderId(list.get(0).getId());
        List<OrderItemData> itemList2 = dto.getByOrderId(list.get(1).getId());
        assertEquals(2, list.size());
        assertEquals(1, itemList1.size());
        assertEquals(1, itemList2.size());
    }

    @Test
    public void invoiceTest() throws Exception {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        inventoryFormList.add(inventoryForm);
        inventoryDto.add(inventoryFormList);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = OrderTestHelper.createForm("a1b2c3d4",5,40599.95);
        orderItemFormList.add(orderItemForm);
        dto.addItem(orderItemFormList);

        List<OrderData> list = dto.getAllOrder();
        InvoiceForm invoiceForm = new InvoiceForm();

//        byte[] expectedBytes = "expected byte array".getBytes();
//        RestTemplate restTemplate = mock(RestTemplate.class);
//        when(restTemplate.postForEntity(url, invoiceForm, byte[].class)).thenReturn(expectedBytes);

//        mockServer = MockRestServiceServer.createServer(restTemplate);
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI(url)))
//                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_PDF)
//                );
//        mockServer.verify();
    }

}
