package com.dev.bruno.dao;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Document;

@Stateless
public class DocumentDAO extends AbstractDAO<Long, Document> {
	
	public String getEntityName() {
		return "Documento";
	}

	public Boolean existsByChave(Long id, String chave) throws MandatoryFieldsException {
		if(chave == null) {
			throw new MandatoryFieldsException("chave é obrigatório");
		}
		
		String hql = "select count(d) from Document d where d.chave = :chave";
		
		if(id != null) {
			hql += " and d.id != :id";
		}
		
		TypedQuery<Long> query = manager.createQuery(hql, Long.class);
		
		query.setParameter("chave", chave);
		
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
		orderOptions.add("captureDate");
		orderOptions.add("baseUrl");
		orderOptions.add("chave");
		
		return orderOptions;
	}
	
	@Override
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("url");
		queryOptions.add("body");
		
		return queryOptions;
	}
	
	public Document findByChave(String chave) throws MandatoryFieldsException {
		if(chave == null) {
			throw new MandatoryFieldsException("chave é obrigatórios");
		}
		
		StringBuilder hql = new StringBuilder("select d from Document d where d.chave = :chave");
		
		return manager.createQuery(hql.toString(), Document.class).setParameter("chave", chave).getSingleResult();
	}
}