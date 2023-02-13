package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandErrorData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class BrandDto {

    @Autowired
    private BrandApi api;

    @Autowired
    private BrandFlowApi flowApi;

    @Autowired
    private CsvFileGenerator csvGenerator;

    public void add(List<BrandForm> forms) throws ApiException {
        List<BrandErrorData> errorData = new ArrayList<>();
        int errorSize = 0;

        for(BrandForm f: forms)
        {
            BrandErrorData brandErrorData = ConvertUtil.convert(f, BrandErrorData.class);
            brandErrorData.setMessage("");
            try
            {
                //todo: validation is kept above as it checks blank and null but if we keep it down @notnull will not be check. how to do this?
                BrandHelper.normalize(f);
                ValidationUtil.validateForms(f);
            }
            catch (ApiException e) {
                errorSize++;
                brandErrorData.setMessage(e.getMessage());
            }
            errorData.add(brandErrorData);
        }
        if (errorSize > 0) {
            ErrorUtil.throwErrors(errorData);
        }

        flowApi.add(forms, errorData);
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
        ValidationUtil.validateForms(f);
        BrandPojo p = BrandHelper.convert(f);
        api.update(id, p);
    }

    public void generateCsv(HttpServletResponse response) throws ApiException {
        response.setContentType("text/csv");

        LocalDateTime lt = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm");
        String identifier = lt.format(dateTimeFormatter);

        response.addHeader("Content-Disposition", "attachment; filename=\"brandReport"+identifier+".csv");
        try {
            csvGenerator.writeBrandsToCsv(api.getAllSorted(), response.getWriter());
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }
}

//todo: rename check and validation functions