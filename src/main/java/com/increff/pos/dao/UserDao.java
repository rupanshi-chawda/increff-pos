package com.increff.pos.dao;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao {

	private static final String DELETE_BY_ID = "delete from UserPojo p where id=:id";
	private static final String SELECT_BY_EMAIL = "select p from UserPojo p where email=:email";

	public void deleteById(int id) {
		Query query = em().createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public UserPojo selectByEmail(String email) {
		TypedQuery<UserPojo> query = getQuery(SELECT_BY_EMAIL, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	public void update(UserPojo p) {
	}


}
