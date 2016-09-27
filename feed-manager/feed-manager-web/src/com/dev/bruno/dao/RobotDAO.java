package com.dev.bruno.dao;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Robot;

@Stateless
public class RobotDAO extends AbstractDAO<Long, Robot> {
	
	public String getEntityName() {
		return "Robô";
	}
	
	public Boolean existsByUrl(Long id, String url) throws MandatoryFieldsException {
		if(url == null) {
			throw new MandatoryFieldsException("url é obrigatório");
		}
		
		String hql = "select count(r) from Robot r where r.url = :url";
		
		if(id != null) {
			hql += " and r.id != :id";
		}
		
		TypedQuery<Long> query = manager.createQuery(hql, Long.class);
		
		query.setParameter("url", url);
		
		if(id != null) {
			query.setParameter("id", id);
		}
		
		Long result = query.getSingleResult();
		
		return result > 0;
	}
	
	@Override
	public Set<String> orderOptions() {
		Set<String> orderOptions = new HashSet<>();
		
		orderOptions.add("id");
		orderOptions.add("name");
		orderOptions.add("url");
		orderOptions.add("documentURLRegex");
		orderOptions.add("sourceURLRegex");
		orderOptions.add("robotGroup.id");
		orderOptions.add("robotGroup.name");
		
		return orderOptions;
	}
	
	@Override
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("name");
		queryOptions.add("url");
		
		return queryOptions;
	}
}