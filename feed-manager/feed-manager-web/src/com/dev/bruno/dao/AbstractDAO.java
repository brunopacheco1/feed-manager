package com.dev.bruno.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import com.dev.bruno.exception.EntityNotFoundException;
import com.dev.bruno.exception.InvalidValueException;
import com.dev.bruno.exception.MandatoryFieldsException;

public abstract class AbstractDAO<PK, T> {

	@PersistenceContext
	protected EntityManager manager;
	
	private Class<T> type;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[1];
	}
	
	public abstract String getEntityName();

	public T findById(PK id) throws EntityNotFoundException, MandatoryFieldsException {
		if(!exists(id)) {
			throw new EntityNotFoundException(getEntityName() + " não encontrado");
		}
		
		T result = manager.find(type, id);
		
		return result;
	}

	public void remove(T entity) throws EntityNotFoundException {
		if(entity == null) {
			throw new EntityNotFoundException(getEntityName() + " não encontrado");
		}
		
		manager.remove(entity);
	}

	public void add(T entity) throws EntityNotFoundException {
		if(entity == null) {
			throw new EntityNotFoundException(getEntityName() + " não encontrado");
		}
		
		manager.persist(entity);
	}
	
	public void update(T entity) throws EntityNotFoundException {
		if(entity == null) {
			throw new EntityNotFoundException(getEntityName() + " não encontrado");
		}
		
		manager.merge(entity);
	}

	public List<T> list(String queryStr, Integer start, Integer limit, String order, String dir) throws MandatoryFieldsException, InvalidValueException {
		if(start == null || limit == null || order == null || dir == null) {
			throw new MandatoryFieldsException("start, limit, order e dir são obrigatórios");
		}
		
		if(!orderOptions().contains(order) || !dirOptions().contains(dir)) {
			throw new InvalidValueException(String.format("Possíveis valores para order[%s] e dir[%s]", StringUtils.join(orderOptions(), ", "), StringUtils.join(dirOptions(), ", ")));
		}
		
		StringBuilder hql = new StringBuilder("select e from " + type.getSimpleName() + " e where 1=1");
		
		
		if(queryStr != null && !queryStr.isEmpty()) {
			hql.append(" and (");
			
			boolean first = true;
			
			for(String queryOption : queryOptions()) {
				if(!first) {
					hql.append(" or ");
				}
				
				hql.append("upper(e.").append(queryOption).append(") like upper(:").append(queryOption).append(")");
				
				first = false;
			}
			
			hql.append(")");
		}
		
		
		hql.append(" order by e." + order + " " + dir);
		
		TypedQuery<T> query = manager.createQuery(hql.toString(), type);
		
		if(queryStr != null && !queryStr.isEmpty()) {
			for(String queryOption : queryOptions()) {
				query.setParameter(queryOption, "%" + queryStr + "%");
			}
		}
		
		return query.setFirstResult(start).setMaxResults(limit).getResultList();
	}
	
	public List<T> list() {
		return manager.createQuery("select e from " + type.getSimpleName() + " e", type).getResultList();
	}

	public Long getCount(String queryStr) {
		
		StringBuilder hql = new StringBuilder("select count(e) from " + type.getSimpleName() + " e where 1=1");
		
		if(queryStr != null && !queryStr.isEmpty()) {
			for(String queryOption : queryOptions()) {
				hql.append(" and e.").append(queryOption).append(" like :").append(queryOption);
			}
		}
		
		TypedQuery<Long> query = manager.createQuery(hql.toString(), Long.class);
		
		if(queryStr != null && !queryStr.isEmpty()) {
			for(String queryOption : queryOptions()) {
				query.setParameter(queryOption, "%" + queryStr + "%");
			}
		}
		
		return query.getSingleResult();
	}
	
	public Boolean exists(PK id) throws MandatoryFieldsException {
		if(id == null) {
			throw new MandatoryFieldsException("id é obrigatório");
		}
		
		Long result = manager.createQuery("select count(e) from " + type.getSimpleName() + " e where e.id = :id", Long.class).setParameter("id", id).getSingleResult();
		
		return result > 0;
	}
	
	public abstract Set<String> orderOptions();
	
	public Set<String> dirOptions() {
		Set<String> dirOptions = new HashSet<>();
		
		dirOptions.add("asc");
		dirOptions.add("desc");
		
		return dirOptions;
	}
	
	public abstract Set<String> queryOptions();
}