package com.increff.pos.util;

import javax.validation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {


    public static <T> void validateForms(T form) throws ApiException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            try{
                throw new ConstraintViolationException(violations);
            }
            catch (ConstraintViolationException ex){
                List<String> details = ex.getConstraintViolations()
                    .parallelStream()
                    .map(e -> e.getPropertyPath() +" "	+ e.getMessage())
                    .collect(Collectors.toList());

                throw new ApiException(details.toString());
            }
        }
    }

}