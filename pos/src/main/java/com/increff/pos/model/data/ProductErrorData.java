package com.increff.pos.model.data;

import com.increff.pos.model.form.ProductForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductErrorData extends ProductForm {

    private String message;
}
