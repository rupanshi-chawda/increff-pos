package com.increff.pos.model.data;

import com.increff.pos.model.form.BrandForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BrandErrorData extends BrandForm {

    private String message;
}