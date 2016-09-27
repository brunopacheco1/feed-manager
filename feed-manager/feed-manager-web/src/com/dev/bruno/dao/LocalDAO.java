package com.dev.bruno.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.dev.bruno.exception.EntityNotFoundException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Local;
import com.dev.bruno.model.LocalSimilar;

@Stateless
public class LocalDAO extends AbstractDAO<Long, Local> {
	
	public String getEntityName() {
		return "Local";
	}
	
	public void add(LocalSimilar localSimilar) throws EntityNotFoundException {
		if(localSimilar == null) {
			throw new EntityNotFoundException("Local Similar não encontrado");
		}
		
		manager.persist(localSimilar);
	}

	public Boolean existsByNome(Long id, String nome) throws MandatoryFieldsException {
		if(nome == null) {
			throw new MandatoryFieldsException("nome é obrigatório");
		}
		
		String hql = "select count(l) from Local l where l.nome = :nome";
		
		if(id != null) {
			hql += " and l.id != :id";
		}
		
		TypedQuery<Long> query = manager.createQuery(hql, Long.class);
		
		query.setParameter("nome", nome);
		
		if(id != null) {
			query.setParameter("id", id);
		}
		
		Long result = query.getSingleResult();
		
		if(result > 0) {
			return true;
		}
		
		hql = "select count(l.local) from LocalSimilar l where l.nome = :nome";
		
		query = manager.createQuery(hql, Long.class);
		
		query.setParameter("nome", nome);

		result = query.getSingleResult();
		
		return result > 0;
	}
	
	public Local findByNome(String nome) throws MandatoryFieldsException {
		if(!existsByNome(null, nome)) {
			return null;
		}
		
		String hql = "select l from Local l where l.nome = :nome";
		
		TypedQuery<Local> query = manager.createQuery(hql, Local.class);
		
		query.setParameter("nome", nome);
		
		Local result = null;
		
		try {
			result = query.getSingleResult();
		} catch (NoResultException e) {}
		
		if(result != null) {
			return result;
		}
		
		hql = "select l.local from LocalSimilar l where l.nome = :nome";
		
		query = manager.createQuery(hql, Local.class);
		
		query.setParameter("nome", nome);

		try {
			result = query.getSingleResult();
		} catch (NoResultException e) {}
		
		return result;
	}
	
	public List<String> listNomes() {
		return manager.createQuery("select l.nome from Local l", String.class).getResultList();
	}
	
	@Override
	public Set<String> orderOptions() {
		Set<String> orderOptions = new HashSet<>();
		
		orderOptions.add("id");
		orderOptions.add("nome");
		
		return orderOptions;
	}
	
	@Override
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("nome");
		
		return queryOptions;
	}
}