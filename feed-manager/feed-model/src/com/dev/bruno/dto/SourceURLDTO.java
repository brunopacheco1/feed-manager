package com.dev.bruno.dto;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class SourceURLDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1869603873653802909L;
	
	@Expose
	private String url;
	
	@Expose
	private String documentURLRegex;
	
	@Expose
	private String sourceURLRegex;
	
	@Expose
	private Long endDepth = 1l;
	
	@Expose
	private Long depth = 1l;
	
	@Expose
	private String ManagerUrl;
	
	@Expose
	private String key;
	
	@Expose
	private Long connectionTimeout = 10000l;
	
	@Expose
	private Long delay = 0l;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Long getDepth() {
		return depth;
	}

	public void setDepth(Long depth) {
		this.depth = depth;
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

	public String getManagerUrl() {
		return ManagerUrl;
	}

	public void setManagerUrl(String managerUrl) {
		ManagerUrl = managerUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}