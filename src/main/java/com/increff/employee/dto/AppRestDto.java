package com.increff.employee.dto;

import com.increff.employee.model.data.MessageData;
import com.increff.employee.util.ApiException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppRestDto {

    public MessageData handle(ApiException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getMessage());
        return data;
    }

    public MessageData handle(Throwable e) {
        MessageData data = new MessageData();
        data.setMessage("An unknown error has occurred - " + e.getMessage());
        return data;
    }
}
