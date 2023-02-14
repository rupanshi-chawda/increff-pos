package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
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

	public <T> T selectById(Integer id, Class<T> z)
	{
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(z);
		Root<T> c = q.from(z);
		ParameterExpression<Integer> p = cb.parameter(Integer.class);
		q.select(c).where(cb.equal(c.get("id"), p));

		TypedQuery<T> query = em().createQuery(q);
		query.setParameter(p, id);
		return getSingle(query);
	}

	public <T> List<T> selectAll(Class<T> z)
	{
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(z);
		Root<T> c = q.from(z);
		q.select(c);

		TypedQuery<T> query = em().createQuery(q);
		return query.getResultList();
	}
}
