package com.dev.bruno.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dev.bruno.dao.SourceDAO;
import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.EntityNotFoundException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Source;
import com.dev.bruno.model.SourceURL;

@Stateless
public class SourceService extends AbstractService {

	@Inject
	private SourceDAO sourceDAO;
	
	
	public Source add(Source source) throws GenericException {
		source.setId(null);
		
		validateSource(source);
		
		for(SourceURL sourceURL : source.getUrls()) {
			sourceURL.setSource(source);
		}
		
		sourceDAO.add(source);
		
		return source;
	}
	
	public Source update(Long id, Source source) throws GenericException {
		source.setId(id);
		
		validateSource(source);
		
		for(SourceURL sourceURL : source.getUrls()) {
			sourceURL.setSource(source);
		}
		
		sourceDAO.update(source);
		
		return source;
	}
	
	public Source get(Long id) throws GenericException {
		return sourceDAO.findById(id);
	}
	
	public List<Source> list(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return sourceDAO.list(queryStr, start, limit, order, dir);
	}
	
	public Long getCount(String queryStr) {
		return sourceDAO.getCount(queryStr);
	}
	
	public void remove(Long id) throws GenericException {
		Source source = sourceDAO.findById(id);
		sourceDAO.remove(source);
	}
	
	public void validateSource(Source source) throws GenericException {
		if(source == null || (source.getId() != null && !sourceDAO.exists(source.getId()))) {
			throw new EntityNotFoundException(sourceDAO.getEntityName() + " não encontrado");
		}
		
		if(source.getName() == null) {
			throw new MandatoryFieldsException("name é um campo obrigatório");
		}
		
		if(source.getUrls() == null || source.getUrls().isEmpty()) {
			throw new MandatoryFieldsException("É preciso ter pelo menos uma URL.");
		}
		
		if(sourceDAO.existsByName(source.getId(), source.getName())) {
			throw new EntityExistsException("Já existe um " + sourceDAO.getEntityName() + " cadastrado com esse nome");
		}
	}

	public boolean existsByName(Long id, String name) throws GenericException {
		return sourceDAO.existsByName(id, name);
	}
}