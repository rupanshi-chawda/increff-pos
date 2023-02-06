package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private InventoryDto dto;

    @Autowired
    private InventoryApi api;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    @Test
    public void addInventoryTest() throws ApiException, JsonProcessingException {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Dyson ");
        brandForm.setCategory(" hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);


        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setBrand("dyson");
        productForm.setCategory("hair");
        productForm.setBarcode(" A1b2C3D4");
        productForm.setName("AirWrap ");
        productForm.setMrp(45000.95);
        productFormList.add(productForm);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("a1b2c3d4");
        inventoryForm.setQuantity(25);
        inventoryFormList.add(inventoryForm);
        dto.add(inventoryFormList);


        String expectedBarcode = "a1b2c3d4";
        int expectedQuantity = 25;

        InventoryData data = dto.get(expectedBarcode);
        assertEquals(expectedBarcode, data.getBarcode());
        assertEquals(expectedQuantity, data.getQuantity());
    }

    @Test
    public void getAllInventoryTest() throws ApiException, JsonProcessingException {

        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Dyson ");
        brandForm.setCategory(" hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setBrand("dyson");
        productForm.setCategory("hair");
        productForm.setBarcode(" A1b2C3D4");
        productForm.setName("AirWrap ");
        productForm.setMrp(45000.95);
        productFormList.add(productForm);

        ProductForm productForm2 = new ProductForm();
        productForm2.setBrand("dyson");
        productForm2.setCategory("hair");
        productForm2.setBarcode(" qwer1234");
        productForm2.setName("superSonic dryer");
        productForm2.setMrp(32000.95);
        productFormList.add(productForm2);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("a1b2c3d4");
        inventoryForm.setQuantity(25);
        inventoryFormList.add(inventoryForm);

        InventoryForm inventoryForm1 = new InventoryForm();
        inventoryForm1.setBarcode("qwer1234");
        inventoryForm1.setQuantity(17);
        inventoryFormList.add(inventoryForm1);
        dto.add(inventoryFormList);

        List<InventoryData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void updateInventoryTest() throws ApiException, JsonProcessingException {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Dyson ");
        brandForm.setCategory(" hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);


        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setBrand("dyson");
        productForm.setCategory("hair");
        productForm.setBarcode(" A1b2C3D4");
        productForm.setName("AirWrap ");
        productForm.setMrp(45000.95);
        productFormList.add(productForm);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("a1b2c3d4");
        inventoryForm.setQuantity(25);
        inventoryFormList.add(inventoryForm);
        dto.add(inventoryFormList);

        InventoryForm inventoryForm1 = new InventoryForm();
        inventoryForm1.setBarcode("a1b2c3d4");
        inventoryForm1.setQuantity(9);
        inventoryFormList.add(inventoryForm1);

        String expectedBarcode = "a1b2c3d4";
        int expectedQty = 9;

        InventoryData data = dto.get(expectedBarcode);
        dto.update(data.getBarcode(), inventoryForm1);

        InventoryPojo pojo = api.getByInventoryId(data.getId());
        assertEquals(expectedQty, pojo.getQuantity());
    }

    @Test
    public void testInventoryReportCsv() throws IOException, ApiException {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Dyson ");
        brandForm.setCategory(" hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);


        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setBrand("dyson");
        productForm.setCategory("hair");
        productForm.setBarcode(" A1b2C3D4");
        productForm.setName("AirWrap ");
        productForm.setMrp(45000.95);
        productFormList.add(productForm);

        ProductForm productForm1 = new ProductForm();
        productForm1.setBrand("dyson");
        productForm1.setCategory("hair");
        productForm1.setBarcode(" qwer1234");
        productForm1.setName("supersonic dryer");
        productForm1.setMrp(32000.55);
        productFormList.add(productForm1);

        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("a1b2c3d4");
        inventoryForm.setQuantity(25);
        inventoryFormList.add(inventoryForm);

        InventoryForm inventoryForm2 = new InventoryForm();
        inventoryForm2.setBarcode("qwer1234");
        inventoryForm2.setQuantity(15);
        inventoryFormList.add(inventoryForm2);
        dto.add(inventoryFormList);

        MockHttpServletResponse response = new MockHttpServletResponse();
        dto.generateCsv(response);

        String fileContent = "Brand\tCategory\tQuantity\n" + "dyson\thair\t40\n";
        assertEquals("text/csv", response.getContentType());
    }

    @Test(expected = ApiException.class)
    public void addIllegalInventory() throws JsonProcessingException, ApiException {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Dyson ");
            brandForm.setCategory(" hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = new ProductForm();
            productForm.setBrand("dyson");
            productForm.setCategory("hair");
            productForm.setBarcode(" A1b2C3D4");
            productForm.setName("AirWrap ");
            productForm.setMrp(45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();

            InventoryForm form = new InventoryForm();
            form.setBarcode("12345679");
            form.setQuantity(3);
            inventoryFormList.add(form);

            dto.add(inventoryFormList);
        }
        catch (ApiException e)
        {
            String exception = "[ {\r\n  \"barcode\" : \"12345679\",\r\n  \"quantity\" : 3,\r\n  \"message\" : \"Product with given barcode does not exists\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test()
    public void addDuplicateInventory() throws JsonProcessingException, ApiException {

        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Dyson ");
        brandForm.setCategory(" hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setBrand("dyson");
        productForm.setCategory("hair");
        productForm.setBarcode(" A1b2C3D4");
        productForm.setName("AirWrap ");
        productForm.setMrp(45000.95);
        productFormList.add(productForm);
        productDto.add(productFormList);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("a1b2c3d4");
        inventoryForm.setQuantity(25);
        inventoryFormList.add(inventoryForm);

        InventoryForm inventoryForm1 = new InventoryForm();
        inventoryForm1.setBarcode("a1b2c3d4");
        inventoryForm1.setQuantity(7);
        inventoryFormList.add(inventoryForm1);

        dto.add(inventoryFormList);

        String expectedBarcode = "a1b2c3d4";
        int expectedQuantity = 32;

        InventoryData data = dto.get(expectedBarcode);
        assertEquals(expectedQuantity, data.getQuantity());
    }

}
