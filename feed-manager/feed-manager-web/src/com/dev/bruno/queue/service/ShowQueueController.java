package com.dev.bruno.queue.service;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Singleton;

import com.dev.bruno.model.Show;
import com.dev.bruno.service.AbstractService;

@Singleton
public class ShowQueueController extends AbstractService {

	private Set<String> shows = new HashSet<>();
	private Set<Long> robotIds = new HashSet<>();
	
	public void add(Show show) {
		shows.add(show.getDocument().getBaseUrl());
	}
	
	public void addRobot(Long robotId) {
		robotIds.add(robotId);
	}
	
	public void remove(Show show) {
		shows.remove(show.getDocument().getBaseUrl());
	}
	
	public Boolean hasShows() {
		return !shows.isEmpty();
	}

	public Set<Long> getRobotIds() {
		return robotIds;
	}

	public void finish() {
		shows.clear();
		shows = new HashSet<>();
		
		robotIds.clear();
		robotIds = new HashSet<>();
	}
}