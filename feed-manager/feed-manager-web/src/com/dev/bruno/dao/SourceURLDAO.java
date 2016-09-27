package com.dev.bruno.dao;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;

import com.dev.bruno.model.SourceURL;

@Stateless
public class SourceURLDAO extends AbstractDAO<Long, SourceURL> {
	
	public String getEntityName() {
		return "Semente";
	}
	
	@Override
	public Set<String> orderOptions() {
		Set<String> orderOptions = new HashSet<>();
		
		orderOptions.add("id");
		orderOptions.add("url");
		orderOptions.add("robot.id");
		orderOptions.add("robot.name");
		
		return orderOptions;
	}
	
	@Override
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("url");
		
		return queryOptions;
	}
}