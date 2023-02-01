package com.increff.pos.dto;

import com.increff.pos.model.data.MessageData;
import com.increff.pos.util.ApiException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Component
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

    public final MessageData handleConstraintViolation(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .parallelStream()
                .map(e -> e.getPropertyPath() +" "	+ e.getMessage())
                .collect(Collectors.toList());

        MessageData data = new MessageData();

        data.setMessage(details.toString());
        return data;
    }
}
