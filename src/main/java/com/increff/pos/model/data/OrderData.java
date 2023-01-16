package com.increff.pos.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.increff.pos.model.form.OrderForm;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderData extends OrderForm {

    private int id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
