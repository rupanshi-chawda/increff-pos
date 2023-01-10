package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class AbstractDao {
	
	@PersistenceContext
	private EntityManager em;

	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}
	
	protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
		return em.createQuery(jpql, clazz);
	}
	
	protected EntityManager em() {
		return em;
	}

	public <T> T selectById(int id, Class<T> clazz, String s) {
		String SELECT_BY_ID = "select p from " + s + " p where id=:id";
		TypedQuery<T> query = getQuery(SELECT_BY_ID, clazz);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public <T> List<T> selectAll(Class<T> clazz, String s) {
		String SELECT_ALL = "select p from " + s + " p";
		TypedQuery<T> query = getQuery(SELECT_ALL, clazz);
		return query.getResultList();
	}

}
