package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.AbstractUnitTest;
import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.helper.BrandTestHelper;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto dto;

    @Autowired
    private BrandApi api;

    @Test
    public void addBrandTest() throws ApiException {
        List<BrandForm> formList = new ArrayList<>();
        BrandForm f = BrandTestHelper.createForm("Dyson ", " hair");
        formList.add(f);

        dto.add(formList);

        String expectedBrand = "dyson";
        String expectedCategory = "hair";
        BrandPojo p = api.getBrandCategory(expectedBrand, expectedCategory);
        assertEquals(expectedBrand, p.getBrand());
        assertEquals(expectedCategory, p.getCategory());
    }

    @Test(expected = ApiException.class)
    public void addDuplicateBrand() throws ApiException {
        try {
            List<BrandForm> formList = new ArrayList<>();
            BrandForm form = BrandTestHelper.createForm("Dyson ", " hair");
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


    @Test(expected = ApiException.class)
    public void addEmptyBrandTest() throws ApiException {
        try {
            List<BrandForm> formList = new ArrayList<>();
            BrandForm form = BrandTestHelper.createForm("dyson", "");
            formList.add(form);

            dto.add(formList);
            dto.add(formList);
        }
        catch(ApiException e)
        {
            String exception = "[ {\r\n  \"brand\" : \"dyson\",\r\n  \"category\" : \"\",\r\n  \"message\" : \"[category must not be blank]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            List<BrandForm> formList = new ArrayList<>();
            BrandForm form = BrandTestHelper.createForm("", "hair");
            formList.add(form);

            dto.add(formList);
            dto.add(formList);
        }
        catch(ApiException e)
        {
            String exception = "[ {\r\n  \"brand\" : \"\",\r\n  \"category\" : \"hair\",\r\n  \"message\" : \"[brand must not be blank]\"\r\n} ]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }


    @Test
    public void getBrandByIdTest() throws ApiException {
        BrandForm form = BrandTestHelper.createForm("dyson", "hair");
        api.add(BrandHelper.convert(form));

        String expectedBrand = "dyson";
        String expectedCategory = "hair";
        BrandPojo p = api.getBrandCategory(expectedBrand, expectedCategory);

        BrandData data = dto.get(p.getId());
        assertEquals(expectedBrand, data.getBrand());
        assertEquals(expectedCategory, data.getCategory());
    }

    @Test
    public  void getAllBrandTest() throws ApiException {
        BrandForm form = BrandTestHelper.createForm("dyson", "hair");
        api.add(BrandHelper.convert(form));

        BrandForm form2 = BrandTestHelper.createForm("vivo ", "phone");
        api.add(BrandHelper.convert(form2));

        List<BrandData> list = dto.getAll();
        assertEquals(2, list.size());
    }

    @Test
    public void updateBrandTest() throws ApiException {
        BrandForm form = BrandTestHelper.createForm("dyson", "hair");
        api.add(BrandHelper.convert(form));

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

    @Test(expected = ApiException.class)
    public void updateDuplicateBrand() throws ApiException {
        try {
            BrandForm form = BrandTestHelper.createForm("dyson", "hair");
            api.add(BrandHelper.convert(form));

            String expectedBrand = "dyson";
            String expectedCategory = "hair";

            BrandPojo p = api.getBrandCategory(expectedBrand,expectedCategory);

            String newBrand = "dyson";
            String newCategory = "hair";
            form.setBrand(newBrand);
            form.setCategory(newCategory);
            dto.update(p.getId(),form);
        }
        catch(ApiException e)
        {
            String exception = "Brand Category already exists";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void updateIllegalBrand() throws ApiException {
        try {
            BrandForm form = BrandTestHelper.createForm("dyson", "hair");
            api.add(BrandHelper.convert(form));

            String expectedBrand = "dyson";
            String expectedCategory = "hair";

            BrandPojo p = api.getBrandCategory(expectedBrand,expectedCategory);

            String newBrand = "vivo";
            String newCategory = "phone";
            form.setBrand(newBrand);
            form.setCategory(newCategory);
            dto.update(98,form);
        }
        catch(ApiException e)
        {
            String exception = "Brand with given ID does not exists, id: 98";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void updateEmptyBrand() throws ApiException {
        try {
            BrandForm form = BrandTestHelper.createForm("dyson", "hair");
            api.add(BrandHelper.convert(form));

            String expectedBrand = "dyson";
            String expectedCategory = "hair";

            BrandPojo p = api.getBrandCategory(expectedBrand,expectedCategory);

            String newBrand = "";
            String newCategory = "phone";
            form.setBrand(newBrand);
            form.setCategory(newCategory);
            dto.update(98,form);
        }
        catch(ApiException e)
        {
            String exception = "[brand must not be blank]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
        try {
            BrandForm form = BrandTestHelper.createForm("dyson", "hair");
            api.add(BrandHelper.convert(form));

            String expectedBrand = "dyson";
            String expectedCategory = "hair";

            BrandPojo p = api.getBrandCategory(expectedBrand,expectedCategory);

            String newBrand = "vivo";
            String newCategory = "";
            form.setBrand(newBrand);
            form.setCategory(newCategory);
            dto.update(98,form);
        }
        catch(ApiException e)
        {
            String exception = "[category must not be blank]";
            assertEquals(exception, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testBrandReportCsv() throws ApiException {
        BrandForm form = BrandTestHelper.createForm("dyson", "hair");
        api.add(BrandHelper.convert(form));

        MockHttpServletResponse response = new MockHttpServletResponse();
        dto.generateCsv(response);
        assertEquals("text/csv", response.getContentType());
    }

}
