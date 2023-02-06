package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.ProductApi;
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
    public void addProductTest() throws ApiException, JsonProcessingException {
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

    @Test
    public void getAllProductTest() throws ApiException, JsonProcessingException {
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
        dto.add(productFormList);

        List<ProductData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void updateProductTest() throws JsonProcessingException, ApiException {
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
    public void addDuplicateProduct() throws JsonProcessingException, ApiException {

        try{
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
    public void addIllegalProduct() throws JsonProcessingException, ApiException {
        try {
            List<ProductForm> productFormList = new ArrayList<>();
            List<BrandForm> brandFormList = new ArrayList<>();
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Dyson ");
            brandForm.setCategory(" hair");
            brandFormList.add(brandForm);


            brandDto.add(brandFormList);

            ProductForm productForm = new ProductForm();
            productForm.setBrand("dyson");
            productForm.setCategory("dryer");
            productForm.setBarcode("a1b2c3d4");
            productForm.setName("airwrap");
            productForm.setMrp(45000.95);
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
}
