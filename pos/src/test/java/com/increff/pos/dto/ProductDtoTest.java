package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.helper.BrandTestHelper;
import com.increff.pos.helper.ProductHelper;
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

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private ProductDto dto;

    @Autowired
    private ProductApi api;

    @Autowired
    private BrandApi brandApi;

    @Test
    public void addProductTest() throws ApiException  {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));


        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","A1B2C3D4", "AirWrap ", 45000.95);
        productFormList.add(productForm);
        dto.add(productFormList);

        Integer expectedBrandCategory = brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory());
        String expectedName = "airwrap";
        Double expectedMrp = 45000.95;
        String expectedBarcode = "a1b2c3d4";


        ProductPojo productPojo = api.get(api.getIdByBarcode(expectedBarcode));

        assertEquals(expectedBarcode, productPojo.getBarcode());
        assertEquals(expectedName, productPojo.getName());
        assertEquals(expectedBrandCategory, productPojo.getBrandCategory());
        assertEquals(expectedMrp, productPojo.getMrp(), 0.001);
    }

    @Test(expected = ApiException.class)
    public void addDuplicateProduct() throws ApiException  {
        try{
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);

            dto.add(productFormList);
            dto.add(productFormList);
        }
        catch(ApiException e)
        {
            String exception = "Product with given barcode already exists";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try{
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm);
            ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair","A1B2C3D4", "AirWrap ", 45000.95);
            productFormList.add(productForm2);

            dto.add(productFormList);
        }
        catch(ApiException e)
        {
            String exception = "Duplicate Barcode Exists";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addIllegalProduct() throws ApiException  {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","dryer","a1b2c3d4", "airwrap", 45000.95);
            productFormList.add(productForm);

            dto.add(productFormList);
        }
        catch(ApiException e)
        {
            String exception = "Brand and Category doesn't exist";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addEmptyProductTest() throws ApiException  {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","","a1b2c3d4", "airwrap", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[category  must be between 1 and 15 characters long , category must not be blank]\"\r\n} ]";
            String exception2 = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[category must not be blank, category  must be between 1 and 15 characters long ]\"\r\n} ]";
            assertThat(e.getMessage(), anyOf(containsString(exception),containsString(exception2)));
            throw e;
        }
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", -1200.00);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"hair\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : -1200.00,\r\n  \"message\" : \"[mrp must be atleast 0]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("","hair","a1b2c3d4", "airwrap", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"\",\r\n  \"category\" : \"hair\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[brand  must be between 1 and 25 characters long , brand must not be blank]\"\r\n} ]";
            String exception2 = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[brand must not be blank, brand  must be between 1 and 25 characters long ]\"\r\n} ]";
            assertThat(e.getMessage(), anyOf(containsString(exception),containsString(exception2)));
            throw e;
        }
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            List<ProductForm> productFormList = new ArrayList<>();
            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "", 45000.95);
            productFormList.add(productForm);
            dto.add(productFormList);

        }
        catch(ApiException e) {
            String exception = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"hair\",\r\n  \"name\" : \"\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[name  must be between 1 and 25 characters long , name must not be blank]\"\r\n} ]";
            String exception2 = "[ {\r\n  \"barcode\" : \"a1b2c3d4\",\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"\",\r\n  \"name\" : \"airwrap\",\r\n  \"mrp\" : 45000.95,\r\n  \"message\" : \"[name must not be blank, name  must be between 1 and 25 characters long ]\"\r\n} ]";
            assertThat(e.getMessage(), anyOf(containsString(exception),containsString(exception2)));
            throw e;
        }
    }

    @Test
    public void getAllProductTest() throws ApiException  {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        api.add(px);

        ProductForm productForm2 = ProductTestHelper.createForm("dyson","hair","qwer1234", "supersonic dryer", 32000.95);
        ProductPojo py = ProductHelper.convert(productForm2);
        py.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm2.getBrand(), productForm2.getCategory()));
        api.add(py);

        List<ProductData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void getProductTest() throws ApiException  {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        api.add(px);

        Integer id = api.getIdByBarcode("a1b2c3d4");
        ProductData d = dto.get(id);

        String expectedBarcode = "a1b2c3d4";
        String expectedName = "airwrap";
        Double expectedMrp = 45000.95;

        assertEquals(expectedBarcode, d.getBarcode());
        assertEquals(expectedName, d.getName());
        assertEquals(expectedMrp, d.getMrp());
    }

    @Test
    public void updateProductTest() throws ApiException  {
        BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
        brandApi.add(BrandHelper.convert(brandForm));

        ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
        ProductPojo px = ProductHelper.convert(productForm);
        px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
        api.add(px);


        String expectedBarcode = "a1b2c3d4";

        Integer id = api.getIdByBarcode(expectedBarcode);
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
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            api.add(px);


            String expectedBarcode = "a1b2c3d4";

            Integer id = api.getIdByBarcode(expectedBarcode);
            ProductUpdateForm updateForm = new ProductUpdateForm();
            String newName = "supersonic wrap";
            Double newMrp = 32000.50;
            updateForm.setName(newName);
            updateForm.setMrp(newMrp);
            dto.update(42, updateForm);
        }
        catch(ApiException e)
        {
            String exception = "Product with given ID does not exists, id: 42";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void updateEmptyBrand() throws ApiException  {
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            api.add(px);


            String expectedBarcode = "a1b2c3d4";

            Integer id = api.getIdByBarcode(expectedBarcode);
            ProductUpdateForm updateForm = new ProductUpdateForm();
            String newName = "";
            Double newMrp = 32000.50;
            updateForm.setName(newName);
            updateForm.setMrp(newMrp);
            dto.update(id, updateForm);
        }
        catch(ApiException e)
        {
            String exception = "[name  must be between 1 and 25 characters long , name must not be blank]";
            String exception2 = "[name must not be blank, name  must be between 1 and 25 characters long ]";
            assertThat(e.getMessage(), anyOf(containsString(exception),containsString(exception2)));
            throw e;
        }
        try {
            BrandForm brandForm = BrandTestHelper.createForm("dyson", "hair");
            brandApi.add(BrandHelper.convert(brandForm));

            ProductForm productForm = ProductTestHelper.createForm("dyson","hair","a1b2c3d4", "airwrap", 45000.95);
            ProductPojo px = ProductHelper.convert(productForm);
            px.setBrandCategory(brandApi.getCheckBrandCategoryId(productForm.getBrand(), productForm.getCategory()));
            api.add(px);

            String expectedBarcode = "a1b2c3d4";

            Integer id = api.getIdByBarcode(expectedBarcode);
            ProductUpdateForm updateForm = new ProductUpdateForm();
            String newName = "supersonic dryer";
            Double newMrp = -32000.50;
            updateForm.setName(newName);
            updateForm.setMrp(newMrp);
            dto.update(id, updateForm);
        }
        catch(ApiException e)
        {
            String exception = "[mrp must be atleast 0]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

}
