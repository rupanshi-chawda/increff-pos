package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.*;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
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
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Test
    public void addInventoryTest() throws ApiException {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        inventoryFormList.add(inventoryForm);
        dto.add(inventoryFormList);


        String expectedBarcode = "a1b2c3d4";
        Integer expectedQuantity = 25;

        InventoryData data = dto.get(expectedBarcode);
        assertEquals(expectedBarcode, data.getBarcode());
        assertEquals(expectedQuantity, data.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void addIllegalInventory() throws ApiException {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

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

        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        inventoryFormList.add(inventoryForm);

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("a1b2c3d4", 7);
        inventoryFormList.add(inventoryForm1);

        dto.add(inventoryFormList);

        String expectedBarcode = "a1b2c3d4";
        Integer expectedQuantity = 32;

        InventoryData data = dto.get(expectedBarcode);
        assertEquals(expectedQuantity, data.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void addEmptyInventory() throws ApiException {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

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
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

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
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair","qwer1234", "supersonic dryer", 32000.95);
        ProductPojo py = ProductHelper.convert(productForm2);
        py.setBrandCategory(brandApi.checkBrandCategory(productForm2.getBrand(), productForm2.getCategory()));
        productApi.add(py);

        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
        api.add(inventoryPojo);

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("qwer1234", 17);
        InventoryPojo inventoryPojo1 = InventoryHelper.convert(inventoryForm1);
        inventoryPojo1.setId(productApi.getIdByBarcode(inventoryForm1.getBarcode()));
        api.add(inventoryPojo1);

        List<InventoryData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void updateInventoryTest() throws ApiException {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
        api.add(inventoryPojo);

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("a1b2c3d4", 9);
        InventoryPojo inventoryPojo1 = InventoryHelper.convert(inventoryForm1);
        inventoryPojo1.setId(productApi.getIdByBarcode(inventoryForm1.getBarcode()));
        api.add(inventoryPojo1);

        String expectedBarcode = "a1b2c3d4";
        Integer expectedQty = 9;

        productApi.checkProductBarcode(expectedBarcode);
        Integer id = productApi.getIdByBarcode(expectedBarcode);
        InventoryData data = InventoryHelper.convert(api.get(id), expectedBarcode);
        dto.update(data.getBarcode(), inventoryForm1);

        InventoryPojo pojo = api.getByInventoryId(data.getId());
        assertEquals(expectedQty, pojo.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void updateIllegalInventory() throws ApiException {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
            productApi.add(px);

            List<InventoryForm> inventoryFormList = new ArrayList<>();
            InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
            InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
            inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
            api.add(inventoryPojo);

            InventoryForm inventoryForm1 = InventoryTestHelper.createForm("a1b2c3d4", -9);
            InventoryPojo inventoryPojo1 = InventoryHelper.convert(inventoryForm1);
            inventoryPojo1.setId(productApi.getIdByBarcode(inventoryForm1.getBarcode()));
            api.add(inventoryPojo1);


            String expectedBarcode = "a1b2c3d4";
            productApi.checkProductBarcode(expectedBarcode);
            Integer id = productApi.getIdByBarcode(expectedBarcode);
            InventoryData data = InventoryHelper.convert(api.get(id), expectedBarcode);
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
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.checkBrandCategory(productForm.getBrand(), productForm.getCategory()));
        productApi.add(px);

        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair","qwer1234", "supersonic dryer", 32000.95);
        ProductPojo py = ProductHelper.convert(productForm2);
        py.setBrandCategory(brandApi.checkBrandCategory(productForm2.getBrand(), productForm2.getCategory()));
        productApi.add(py);


        InventoryForm inventoryForm = InventoryTestHelper.createForm("a1b2c3d4", 25);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        inventoryPojo.setId(productApi.getIdByBarcode(inventoryForm.getBarcode()));
        api.add(inventoryPojo);

        InventoryForm inventoryForm1 = InventoryTestHelper.createForm("qwer1234", 15);
        InventoryPojo inventoryPojo1 = InventoryHelper.convert(inventoryForm1);
        inventoryPojo1.setId(productApi.getIdByBarcode(inventoryForm1.getBarcode()));
        api.add(inventoryPojo1);


        MockHttpServletResponse response = new MockHttpServletResponse();
        dto.generateCsv(response);
        assertEquals("text/csv", response.getContentType());
    }

}
