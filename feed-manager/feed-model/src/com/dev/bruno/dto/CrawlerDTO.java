package com.dev.bruno.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dev.bruno.model.SourceURL;
import com.google.gson.annotations.Expose;

public class CrawlerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1479634687315000297L;

	@Expose
	private String key;
	
	@Expose
	private String managerUrl;
	
	@Expose
	private String documentURLRegex;
	
	@Expose
	private String sourceURLRegex;
	
	@Expose
	private Long endDepth = 1l;
	
	@Expose
	private Long connectionTimeout = 10000l;

	@Expose
	private Long delay = 0l;
	
	@Expose
	private List<SourceURL> urls = new ArrayList<>();
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getManagerUrl() {
		return managerUrl;
	}

	public void setManagerUrl(String managerUrl) {
		this.managerUrl = managerUrl;
	}

	public String getDocumentURLRegex() {
		return documentURLRegex;
	}

	public void setDocumentURLRegex(String documentURLRegex) {
		this.documentURLRegex = documentURLRegex;
	}

	public String getSourceURLRegex() {
		return sourceURLRegex;
	}

	public void setSourceURLRegex(String sourceURLRegex) {
		this.sourceURLRegex = sourceURLRegex;
	}

	public Long getEndDepth() {
		return endDepth;
	}

	public void setEndDepth(Long endDepth) {
		this.endDepth = endDepth;
	}

	public Long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Long getDelay() {
		return delay;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	public List<SourceURL> getURLs() {
		return urls;
	}

	public void setURLs(List<SourceURL> sourceURLs) {
		this.urls = sourceURLs;
	}
}