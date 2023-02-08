package com.increff.pos.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderData
{
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime time;
}
