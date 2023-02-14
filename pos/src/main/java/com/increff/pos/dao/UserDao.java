package com.increff.pos.dao;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import com.increff.pos.pojo.UserPojo;
import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractDao {

	private static final String DELETE_BY_ID = "delete from UserPojo p where id=:id";

	public void deleteById(Integer id) {
		Query query = em().createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public UserPojo selectByEmail(String email) {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<UserPojo> q = cb.createQuery(UserPojo.class);
		Root<UserPojo> c = q.from(UserPojo.class);
		ParameterExpression<String > p = cb.parameter(String.class);
		q.select(c).where(cb.equal(c.get("email"), p));

		TypedQuery<UserPojo> query = em().createQuery(q);
		query.setParameter(p, email);
		return getSingle(query);
	}

	public void update(UserPojo p) {
	}


}
