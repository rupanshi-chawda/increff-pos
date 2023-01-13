package com.increff.pos.dto;

import com.increff.pos.model.data.MessageData;
import com.increff.pos.util.ApiException;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
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