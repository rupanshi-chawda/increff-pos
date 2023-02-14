package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.AdminDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.util.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/api/admin/user")
public class AdminController {

	@Autowired
	private AdminDto dto;

	@ApiOperation(value = "Adds a user")
	@PostMapping(path = "")
	public void addUser(@RequestBody UserForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Deletes a user")
	@DeleteMapping(path = "/{id}")
	public void deleteUser(@PathVariable Integer id) {
		dto.delete(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@GetMapping(path = "")
	public List<UserData> getAllUser() {
		return dto.getAll();
	}

}
