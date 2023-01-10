package com.increff.employee.service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.employee.dto.UserDto;
import com.increff.employee.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Autowired
	private UserDto dto;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		UserDto.normalize(p);
		dto.getCheck(p);
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll(UserPojo.class, "UserPojo");
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

}
