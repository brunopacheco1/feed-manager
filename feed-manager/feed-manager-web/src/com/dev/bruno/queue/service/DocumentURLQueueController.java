package com.dev.bruno.queue.service;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Singleton;

import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.service.AbstractService;

@Singleton
public class DocumentURLQueueController extends AbstractService {

	private Set<String> urls = new HashSet<>();
	
	public void add(DocumentURL url) {
		urls.add(url.getUrl());
	}
	
	public void remove(DocumentURL url) {
		urls.remove(url.getUrl());
	}
	
	public Boolean hasURLs() {
		return !urls.isEmpty();
	}

	public void finish() {
		urls.clear();
		urls = new HashSet<>();
	}
}