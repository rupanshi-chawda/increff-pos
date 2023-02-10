package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.helper.BrandTestHelper;
import com.increff.pos.helper.InventoryTestHelper;
import com.increff.pos.helper.ProductTestHelper;
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
    public void addInventoryTest() throws ApiException {
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
        dto.add(inventoryFormList);


        String expectedBarcode = "a1b2c3d4";
        int expectedQuantity = 25;

        InventoryData data = dto.get(expectedBarcode);
        assertEquals(expectedBarcode, data.getBarcode());
        assertEquals(expectedQuantity, data.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void addIllegalInventory() throws ApiException {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();

            InventoryForm form = InventoryTestHelper.createForm("12345679", 3);
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
    public void addDuplicateInventory() throws ApiException {

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

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("a1b2c3d4", 7);
        inventoryFormList.add(inventoryForm1);

        dto.add(inventoryFormList);

        String expectedBarcode = "a1b2c3d4";
        int expectedQuantity = 32;

        InventoryData data = dto.get(expectedBarcode);
        assertEquals(expectedQuantity, data.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void addEmptyInventory() throws ApiException {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();

            InventoryForm form = InventoryTestHelper.createForm("ab23fg4", 3);
            inventoryFormList.add(form);

            dto.add(inventoryFormList);
        }
        catch (ApiException e)
        {
            String exception = "[ {\r\n  \"barcode\" : \"ab23fg4\",\r\n  \"quantity\" : 3,\r\n  \"message\" : \"[barcode must be 8 character long]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            productDto.add(productFormList);

            List<InventoryForm> inventoryFormList = new ArrayList<>();

            InventoryForm form = InventoryTestHelper.createForm("a1b2c3d4", -17);
            inventoryFormList.add(form);

            dto.add(inventoryFormList);
        }
        catch (ApiException e)
        {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"quantity\" : -17,\r\n  \"message\" : \"[quantity must be atleast 1]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test
    public void getAllInventoryTest() throws ApiException {

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

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("qwer1234", 17);
        inventoryFormList.add(inventoryForm1);
        dto.add(inventoryFormList);

        List<InventoryData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void updateInventoryTest() throws ApiException {
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
        dto.add(inventoryFormList);

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("a1b2c3d4", 9);
        inventoryFormList.add(inventoryForm1);

        String expectedBarcode = "a1b2c3d4";
        int expectedQty = 9;

        InventoryData data = dto.get(expectedBarcode);
        dto.update(data.getBarcode(), inventoryForm1);

        InventoryPojo pojo = api.getByInventoryId(data.getId());
        assertEquals(expectedQty, pojo.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void updateIllegalInventory() throws ApiException {
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
            dto.add(inventoryFormList);

            InventoryForm inventoryForm1 = InventoryTestHelper.createForm("a1b2c3d4", -9);
            inventoryFormList.add(inventoryForm1);

            String expectedBarcode = "a1b2c3d4";
            InventoryData data = dto.get(expectedBarcode);
            dto.update(data.getBarcode(), inventoryForm1);
        }
        catch (ApiException e)
        {
            String exception = "[quantity must be atleast 1]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }


    @Test
    public void testInventoryReportCsv() throws ApiException {
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

        InventoryForm inventoryForm2 = InventoryTestHelper.createForm("qwer1234", 15);
        inventoryFormList.add(inventoryForm2);
        dto.add(inventoryFormList);

        MockHttpServletResponse response = new MockHttpServletResponse();
        dto.generateCsv(response);
        assertEquals("text/csv", response.getContentType());
    }

}
