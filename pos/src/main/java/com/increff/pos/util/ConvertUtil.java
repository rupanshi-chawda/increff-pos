package com.increff.pos.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ConvertUtil {
    public static <T, R> R convert(T fromClass, Class<R> toClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // QUES : Where should this be added?
        mapper.registerModule(new JavaTimeModule());
        R newObject = mapper.convertValue(fromClass, toClass);
        return newObject;
    }
}