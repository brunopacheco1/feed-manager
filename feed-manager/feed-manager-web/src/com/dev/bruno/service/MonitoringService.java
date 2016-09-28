package com.dev.bruno.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import com.dev.bruno.dao.MonitoringDAO;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Monitoring;

@Stateless
public class MonitoringService extends AbstractService {

	@Inject
	private MonitoringDAO monitoringDAO;
	
	
	public Monitoring add(Monitoring monitoring) throws GenericException {
		monitoring.setId(null);
		
		validateMonitoring(monitoring);
		
		monitoringDAO.add(monitoring);
		
		return monitoring;
	}
	
	public Monitoring get(Long id) throws GenericException {
		return monitoringDAO.findById(id);
	}
	
	public List<Monitoring> list(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return monitoringDAO.list(queryStr, start, limit, order, dir);
	}
	
	public Long getCount(String queryStr) {
		return monitoringDAO.getCount(queryStr);
	}
	
	public void validateMonitoring(Monitoring monitoring) throws GenericException {
		if(monitoring == null) {
			throw new EntityNotFoundException(monitoringDAO.getEntityName() + " não encontrado");
		}
		
		if(monitoring.getEndDate() == null || monitoring.getExecutionTime() == null || monitoring.getIp() == null || monitoring.getMethod() == null || monitoring.getPath() == null || monitoring.getResponseStatus() == null || monitoring.getResponseType() == null || monitoring.getStartDate() == null) {
			throw new MandatoryFieldsException("endDate, executionTime, ip, method, path, responseStatus, responseType e startDate são campos obrigatórios");
		}
	}
}