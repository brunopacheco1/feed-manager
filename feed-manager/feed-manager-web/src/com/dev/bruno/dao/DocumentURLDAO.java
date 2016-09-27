package com.dev.bruno.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import com.dev.bruno.exception.InvalidValueException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.model.Robot;

@Stateless
public class DocumentURLDAO extends AbstractDAO<Long, DocumentURL> {
	
	public String getEntityName() {
		return "URL de Documento";
	}

	@Override
	public Set<String> orderOptions() {
		Set<String> orderOptions = new HashSet<>();
		
		orderOptions.add("id");
		orderOptions.add("normalizationDate");
		orderOptions.add("url");
		orderOptions.add("robot.id");
		orderOptions.add("robot.name");
		orderOptions.add("robot.url");
		orderOptions.add("robot.robotGroup.id");
		orderOptions.add("robot.robotGroup.name");
		
		return orderOptions;
	}
	
	public Boolean existsByUrl(Long id, String url) throws MandatoryFieldsException {
		if(url == null) {
			throw new MandatoryFieldsException("url é obrigatório");
		}
		
		String hql = "select count(u) from DocumentURL u where u.url = :url";
		
		if(id != null) {
			hql += " and u.id != :id";
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
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("url");
		
		return queryOptions;
	}
	
	public List<DocumentURL> list(Robot robot, Boolean normalized, Integer start, Integer limit, String order, String dir) throws MandatoryFieldsException, InvalidValueException {
		if(robot == null || start == null || limit == null || order == null || dir == null) {
			throw new MandatoryFieldsException("robot, status, start, limit, order e dir são obrigatórios");
		}
		
		if(normalized == null) {
			normalized = false;
		}
		
		if(!orderOptions().contains(order) || !dirOptions().contains(dir)) {
			throw new InvalidValueException(String.format("Possíveis valores para order[%s] e dir[%s]", StringUtils.join(orderOptions(), ", "), StringUtils.join(dirOptions(), ", ")));
		}
		
		StringBuilder hql = new StringBuilder("select u from DocumentURL u where u.robot = :robot");
		
		if(normalized) {
			hql.append(" and u.documents IS NOT EMPTY");
		} else {
			hql.append(" and u.documents IS EMPTY");
		}
		
		hql.append(" order by u." + order + " " + dir);
		
		List<DocumentURL> urls = manager.createQuery(hql.toString(), DocumentURL.class).setParameter("robot", robot).setFirstResult(start).setMaxResults(limit).getResultList();
		
		return urls;
	}
	
	public Long getCount(Robot robot, Boolean normalized) throws MandatoryFieldsException {
		if(robot == null) {
			throw new MandatoryFieldsException("robot e status são obrigatórios");
		}
		
		StringBuilder hql = new StringBuilder("select count(u) from DocumentURL u where u.robot = :robot");
		
		if(normalized == null) {
			normalized = false;
		}
		
		if(normalized) {
			hql.append(" and u.documents IS NOT EMPTY");
		} else {
			hql.append(" and u.documents IS EMPTY");
		}
		
		return manager.createQuery(hql.toString(), Long.class).setParameter("robot", robot).getSingleResult();
	}
	
	public DocumentURL findByURL(String url) throws MandatoryFieldsException {
		if(url == null) {
			throw new MandatoryFieldsException("url é obrigatórios");
		}
		
		StringBuilder hql = new StringBuilder("select u from DocumentURL u where u.url = :url");
		
		return manager.createQuery(hql.toString(), DocumentURL.class).setParameter("url", url).getSingleResult();
	}
}