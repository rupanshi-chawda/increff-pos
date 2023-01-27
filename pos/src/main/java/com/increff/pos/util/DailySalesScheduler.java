package com.increff.pos.util;

import com.increff.pos.dto.SalesDto;
import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
public class DailySalesScheduler {
    @Autowired
    SalesDto salesDto;

    @Async
    @Scheduled(cron = "40 13 10 * * *")
    public void createReport() throws ApiException {
        salesDto.createReport();;
    }
}