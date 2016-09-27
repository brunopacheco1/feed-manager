package com.dev.bruno.dao;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;

import com.dev.bruno.model.Monitoring;

@Stateless
public class MonitoringDAO extends AbstractDAO<Long, Monitoring> {
	
	public String getEntityName() {
		return "Registro de Monitoramento";
	}

	@Override
	public Set<String> orderOptions() {
		Set<String> orderOptions = new HashSet<>();
		
		orderOptions.add("id");
		orderOptions.add("ip");
		orderOptions.add("token");
		orderOptions.add("method");
		orderOptions.add("path");
		orderOptions.add("startDate");
		orderOptions.add("endDate");
		orderOptions.add("executionTime");
		orderOptions.add("requestType");
		orderOptions.add("responseStatus");
		orderOptions.add("responseType");
		
		return orderOptions;
	}
	
	@Override
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("token");
		queryOptions.add("method");
		queryOptions.add("path");
		
		return queryOptions;
	}
}