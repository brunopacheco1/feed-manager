package com.dev.bruno.dto;

import java.io.Serializable;
import java.util.List;

import com.dev.bruno.model.DocumentURL;
import com.google.gson.annotations.Expose;

public class NormalizerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -289144448358376459L;
	
	@Expose
	private List<DocumentURL> urls;
	
	@Expose
	private String managerUrl;
	
	@Expose
	private String key;
	
	@Expose
	private Long connectionTimeout = 10000l;
	
	@Expose
	private Long delay = 0l;

	public Long getDelay() {
		return delay;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	public String getManagerUrl() {
		return managerUrl;
	}

	public void setManagerUrl(String managerUrl) {
		this.managerUrl = managerUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public List<DocumentURL> getUrls() {
		return urls;
	}

	public void setUrls(List<DocumentURL> urls) {
		this.urls = urls;
	}
}