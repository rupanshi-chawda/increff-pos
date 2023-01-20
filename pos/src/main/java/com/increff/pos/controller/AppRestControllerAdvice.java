package com.increff.pos.controller;

import com.increff.pos.dto.AppRestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.increff.pos.model.data.MessageData;
import com.increff.pos.util.ApiException;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@Autowired
	private AppRestDto dto;

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(ApiException e) {
		return dto.handle(e);
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		return dto.handle(e);
	}
}