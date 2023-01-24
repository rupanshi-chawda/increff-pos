package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.ApiException;
import com.increff.pos.util.CsvFileGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class BrandDto {

    @Autowired
    private BrandApi api;

    @Autowired
    private CsvFileGenerator csvGenerator;

    public void add(BrandForm form) throws ApiException {
        BrandHelper.normalize(form);
        BrandHelper.validate(form);
        BrandPojo p = BrandHelper.convert(form);
        api.add(p);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo p = api.get(id);
        return BrandHelper.convert(p);
    }

    public List<BrandData> getAll() {
        return api.getAll().stream().map(BrandHelper::convert).collect(Collectors.toList());
    }

    public void update(int id, BrandForm f) throws ApiException {
        BrandHelper.normalize(f);
        BrandHelper.validate(f);
        BrandPojo p = BrandHelper.convert(f);
        api.update(id, p);
    }

    public void generateCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"brandReport.csv\"");
        csvGenerator.writeBrandsToCsv(api.getAll(), response.getWriter());
    }
}
