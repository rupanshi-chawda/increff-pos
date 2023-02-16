package com.increff.pos.dto;

import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.data.BrandErrorData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.util.*;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandDto {

    @Autowired
    private BrandApi api;

    @Autowired
    private BrandFlowApi flowApi;

    @Autowired
    private CsvFileGenerator csvGenerator;

    public void add(List<BrandForm> forms) throws ApiException {
        checkDuplicates(forms);
        BrandHelper.validateFormLists(forms);
        flowApi.add(forms);
    }

    public BrandData get(Integer id) throws ApiException {
        BrandPojo p = api.get(id);
        return BrandHelper.convert(p);
    }

    public List<BrandData> getAll() {
        return api.getAllSorted().stream().map(BrandHelper::convert).collect(Collectors.toList());
    }

    public void update(Integer id, BrandForm f) throws ApiException {
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

    private void checkDuplicates(List<BrandForm> forms) throws ApiException {
        HashSet<Pair<String, String>> set = new HashSet<>();
        for(BrandForm form : forms) {
            Pair<String, String> p = new Pair<>(form.getBrand(), form.getCategory());
            if (set.contains(p)) {
                throw new ApiException("Duplicates Brand and Category Exists");
            }
            set.add(p);
        }
    }
}
