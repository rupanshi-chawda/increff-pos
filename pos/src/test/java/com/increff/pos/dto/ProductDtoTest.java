package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.BrandTestHelper;
import com.increff.pos.helper.ProductTestHelper;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.ProductUpdateForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private ProductDto dto;

    @Autowired
    private ProductApi api;

    @Autowired
    private BrandDto brandDto;

    @Test
    public void addProductTest() throws ApiException  {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);


        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        dto.add(productFormList);

        String expectedBrand = "dyson";
        String expectedCategory = "hair";
        String expectedName = "airwrap";
        Double expectedMrp = 45000.95;
        String expectedBarcode = "a1b2c3d4";


        ProductData productData = dto.get(api.getIdByBarcode(expectedBarcode));

        assertEquals(expectedBarcode, productData.getBarcode());
        assertEquals(expectedName, productData.getName());
        assertEquals(expectedBrand, productData.getBrand());
        assertEquals(expectedCategory, productData.getCategory());
        assertEquals(expectedMrp, productData.getMrp(), 0.001);
    }

    @Test(expected = ApiException.class)
    public void addDuplicateProduct() throws ApiException  {

        try{
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);

            dto.add(productFormList);
            dto.add(productFormList);
        }
        catch(ApiException e)
        {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"hair\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"Product with given barcode already exists\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addIllegalProduct() throws ApiException  {
        try {
            List<ProductForm> productFormList = new ArrayList<>();
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);


            brandDto.add(brandFormList);

            ProductForm productForm = ProductTestHelper.createForm("dyson","dryer","a1b2c3d4", "airwrap", 45000.95);
            productFormList.add(productForm);

            dto.add(productFormList);
        }
        catch(ApiException e)
        {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"dryer\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"Brand and Category doesn't exist\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addEmptyProductTest() throws ApiException  {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","","a1b2c3d4", "airwrap", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[category must not be blank]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", -1200.00);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"hair\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : -1200.00,\r\n  \"message\" : \"[mrp must be atleast 1]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("","hair","a1b2c3d4", "airwrap", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"\",\r\n  \"category\" : \"hair\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[brand must not be blank]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);


            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"hair\",\r\n  \"name\" : \"\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[name must not be blank]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test
    public void getAllProductTest() throws ApiException  {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);

        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair"," qwer1234", "superSonic dryer", 32000.95);
        productFormList.add(productForm2);
        dto.add(productFormList);

        List<ProductData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void updateProductTest() throws ApiException  {
        List<BrandForm> brandFormList = new ArrayList<>();
        BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
        brandFormList.add(brandForm);
        brandDto.add(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        dto.add(productFormList);


        String expectedBarcode = "a1b2c3d4";

        int id = api.getIdByBarcode(expectedBarcode);
        ProductUpdateForm updateForm = new ProductUpdateForm();
        String newName = "supersonic wrap";
        Double newMrp = 32000.50;
        updateForm.setName(newName);
        updateForm.setMrp(newMrp);
        dto.update(id, updateForm);

        ProductPojo p = api.get(id);
        assertEquals(newMrp, p.getMrp());
        assertEquals(newName, p.getName());
    }


    @Test(expected = ApiException.class)
    public void updateIllegalProduct() throws ApiException  {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);


            String expectedBarcode = "a1b2c3d4";

            int id = api.getIdByBarcode(expectedBarcode);
            ProductUpdateForm updateForm = new ProductUpdateForm();
            String newName = "supersonic wrap";
            Double newMrp = 32000.50;
            updateForm.setName(newName);
            updateForm.setMrp(newMrp);
            dto.update(42, updateForm);
        }
        catch(ApiException    e)
        {
            String exception = "Product with given ID does not exit, id: 42";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void updateEmptyBrand() throws ApiException  {
        try {
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = BrandTestHelper.createForm("Dyson ", " hair");
            brandFormList.add(brandForm);
            brandDto.add(brandFormList);

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair"," A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);


            String expectedBarcode = "a1b2c3d4";

            int id = api.getIdByBarcode(expectedBarcode);
            ProductUpdateForm updateForm = new ProductUpdateForm();
            String newName = "";
            Double newMrp = 32000.50;
            updateForm.setName(newName);
            updateForm.setMrp(newMrp);
            dto.update(id, updateForm);
        }
        catch(ApiException e)
        {
            String exception = "[name must not be blank]";
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
            dto.add(productFormList);


            String expectedBarcode = "a1b2c3d4";

            int id = api.getIdByBarcode(expectedBarcode);
            ProductUpdateForm updateForm = new ProductUpdateForm();
            String newName = "supersonic dryer";
            Double newMrp = -32000.50;
            updateForm.setName(newName);
            updateForm.setMrp(newMrp);
            dto.update(id, updateForm);
        }
        catch(ApiException e)
        {
            String exception = "[mrp must be atleast 1]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

}
