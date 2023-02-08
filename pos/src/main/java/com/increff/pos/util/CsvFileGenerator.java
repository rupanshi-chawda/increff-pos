package com.increff.pos.util;

import com.increff.pos.model.commons.InventoryItem;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import io.swagger.annotations.Api;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

@Component
public class CsvFileGenerator {

    public void writeBrandsToCsv(List<BrandPojo> brands, Writer writer) throws ApiException {
        try {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
                printer.printRecord("Brand","Category");
            for (BrandPojo b : brands) {
                printer.printRecord(b.getBrand(), b.getCategory());
            }
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public void writeInventoryToCsv(List<InventoryItem> inventory, Writer writer) throws ApiException {
        try {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            printer.printRecord("Brand","Category","Quantity");
            for (InventoryItem i: inventory) {
                printer.printRecord(i.getBrand(), i.getCategory(), i.getQuantity());
            }
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public void writeSalesToCsv(List<SalesReportData> sales, PrintWriter writer) throws ApiException{
        try {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            printer.printRecord("Brand","Category","Quantity","Revenue");
            for (SalesReportData i: sales) {
                printer.printRecord(i.getBrand(), i.getCategory(), i.getQuantity(), i.getRevenue());
            }
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
