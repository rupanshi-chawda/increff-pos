package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.BrandApi;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto dto;

    @Autowired
    private BrandApi api;

    @Test
    public void addBrandTest() throws ApiException, JsonProcessingException {
        List<BrandForm> formList = new ArrayList<>();
        BrandForm f = new BrandForm();
        f.setBrand("Dyson ");
        f.setCategory(" hair");
        formList.add(f);

        dto.add(formList);

        String expectedBrand = "dyson";
        String expectedCategory = "hair";
        BrandPojo p = api.getBrandCategory(expectedBrand, expectedCategory);
        assertEquals(expectedBrand, p.getBrand());
        assertEquals(expectedCategory, p.getCategory());
    }

    @Test
    public void getBrandByIdTest() throws ApiException, JsonProcessingException {
        List<BrandForm> formList = new ArrayList<>();
        BrandForm form = new BrandForm();
        form.setBrand("Dyson ");
        form.setCategory(" hair");
        formList.add(form);

        dto.add(formList);

        String expectedBrand = "dyson";
        String expectedCategory = "hair";
        BrandPojo p = api.getBrandCategory(expectedBrand, expectedCategory);

        BrandData data = dto.get(p.getId());
        assertEquals(expectedBrand, data.getBrand());
        assertEquals(expectedCategory, data.getCategory());
    }

    @Test
    public  void getAllBrandTest() throws JsonProcessingException, ApiException {
        List<BrandForm> formList = new ArrayList<>();
        BrandForm form = new BrandForm();
        form.setBrand(" Dyson");
        form.setCategory(" hair ");
        formList.add(form);

        BrandForm form2 = new BrandForm();
        form2.setBrand(" Vivo ");
        form2.setCategory("phone ");
        formList.add(form2);

        dto.add(formList);

        List<BrandData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void updateBrandTest() throws JsonProcessingException, ApiException {
        List<BrandForm> formList = new ArrayList<>();
        BrandForm form = new BrandForm();
        form.setBrand(" Dyson ");
        form.setCategory("haiR");
        formList.add(form);

        dto.add(formList);

        String expectedBrand = "dyson";
        String expectedCategory = "hair";

        BrandPojo p = api.getBrandCategory(expectedBrand,expectedCategory);

        String newBrand = "vivo";
        String newCategory = "phone";
        form.setBrand(newBrand);
        form.setCategory(newCategory);
        dto.update(p.getId(),form);

        BrandPojo b = api.getCheckBrandId(p.getId());
        assertEquals(newBrand, b.getBrand());
        assertEquals(newCategory, b.getCategory());
    }

    @Test
    public void testBrandReportCsv() throws IOException, ApiException {
        List<BrandForm> formList = new ArrayList<>();
        BrandForm form = new BrandForm();
        form.setBrand(" Dyson");
        form.setCategory("hair ");
        formList.add(form);

        dto.add(formList);

        MockHttpServletResponse response = new MockHttpServletResponse();
        dto.generateCsv(response);

        String fileContent = "Brand\tCategory\n" + "dyson\thair\n";
        assertEquals("text/csv", response.getContentType());
        //assertEquals(fileContent, response.getContentAsString());
    }

    @Test(expected = ApiException.class)
    public void addDuplicateBrand() throws JsonProcessingException, ApiException {
        try {
            List<BrandForm> formList = new ArrayList<>();
            BrandForm form = new BrandForm();
            form.setBrand("Dyson ");
            form.setCategory(" hair");
            formList.add(form);

            dto.add(formList);
            dto.add(formList);
        }
        catch(ApiException e)
        {
            String exception = "[ {\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"hair\",\r\n  \"message\" : \"Brand Category already exists\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }
}
