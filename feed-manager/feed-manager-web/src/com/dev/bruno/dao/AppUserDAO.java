package com.dev.bruno.dao;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.AppUser;

@Stateless
public class AppUserDAO extends AbstractDAO<Long, AppUser> {
	
	public String getEntityName() {
		return "Usuário do Sistema";
	}

	@Override
	public Set<String> orderOptions() {
		Set<String> orderOptions = new HashSet<>();
		
		orderOptions.add("id");
		orderOptions.add("name");
		orderOptions.add("login");
		orderOptions.add("userType");
		
		return orderOptions;
	}

	public Boolean existsByLogin(Long id, String login) throws MandatoryFieldsException {
		if(login == null) {
			throw new MandatoryFieldsException("login é obrigatório");
		}
		
		String hql = "select count(u) from AppUser u where u.login = :login";
		
		if(id != null) {
			hql += " and u.id != :id";
		}
		
		TypedQuery<Long> query = manager.createQuery(hql, Long.class);
		
		query.setParameter("login", login);
		
		if(id != null) {
			query.setParameter("id", id);
		}
		
		Long result = query.getSingleResult();
		
		return result > 0;
	}
	
	public AppUser findByLogin(String login) throws MandatoryFieldsException {
		if(login == null) {
			throw new MandatoryFieldsException("login é obrigatório");
		}
		
		try {
			return manager.createQuery("select u from AppUser u where u.login = :login", AppUser.class).setParameter("login", login).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("name");
		queryOptions.add("login");
		
		return queryOptions;
	}
}