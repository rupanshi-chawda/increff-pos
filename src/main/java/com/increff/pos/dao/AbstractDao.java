package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
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

	@Transactional
	public <T> void insert(T t){
		em.persist(t);
	}

	public <T> T selectById(int id, Class<T> clazz) {
		String SELECT_BY_ID = "select p from " + clazz.getName() + " p where id=:id";
		TypedQuery<T> query = getQuery(SELECT_BY_ID, clazz);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public <T> List<T> selectAll(Class<T> clazz) {
		String SELECT_ALL = "select p from " + clazz.getName() + " p";
		TypedQuery<T> query = getQuery(SELECT_ALL, clazz);
		return query.getResultList();
	}
}
