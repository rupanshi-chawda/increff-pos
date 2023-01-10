package com.increff.employee.service;

import java.util.List;
import javax.transaction.Transactional;

import com.increff.employee.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.selectByEmail(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll(UserPojo.class, "UserPojo");
	}

	@Transactional
	public void delete(int id) {
		dao.deleteById(id);
	}


	public UserPojo getUserEmail(UserPojo p) {
		return dao.selectByEmail(p.getEmail());
	}
}
