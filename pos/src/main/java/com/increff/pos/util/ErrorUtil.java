package com.increff.pos.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.pos.util.ApiException;

import java.util.List;

public class ErrorUtil {
//    public static  <T> void throwErrors(List<T> errors) throws ApiException, JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors);
//        throw new ApiException(json);
//    }


    public static  <T> void throwErrors(List<T> errors) throws ApiException {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errors);
        } catch (JsonProcessingException e) {
            throw new ApiException(e.getMessage());
        }
        throw new ApiException(json);
    }
}