package com.increff.pos.spring;

import com.increff.pos.dto.SalesJobDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
public class SchedulerConfig {
    @Autowired
    SalesJobDto salesJobDto;

    @Async
    @Scheduled(cron = "00 00 12 * * *") //runs at 12 pm noon everyday
    public void createReport() {
        salesJobDto.createReport();
    }
}