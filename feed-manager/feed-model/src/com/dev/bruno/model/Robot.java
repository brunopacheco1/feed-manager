package com.dev.bruno.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

@Entity
@Table(name="ROBO")
public class Robot implements Serializable {

	private static final long serialVersionUID = 8269904635290961631L;
	
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_ROBO")
	private Long id;
	
	@Expose
	@Column(name="DSC_NOME",nullable=false,length=1000)
	private String name;
	
	@Expose
	@Column(name="DSC_URL",nullable=false,length=1000)
	private String url;
	
	@Expose
	@Column(name="DSC_REGEX_URL_DOCUMENTO",nullable=false,length=1000)
	private String documentURLRegex;
	
	@Expose
	@Column(name="DSC_REGEX_URL_FONTE",length=1000)
	private String sourceURLRegex;
	
	@Expose
	@Column(name="NRO_PROFUNDIDADE_MAX",nullable=false)
	private Long endDepth = 1l;
	
	@Expose
	@Column(name="NRO_TIMEOUT_CONEXAO",nullable=false)
	private Long connectionTimeout = 10000l;
	
	@Expose
	@Column(name="NRO_DELAY",nullable=false)
	private Long delay = 0l;
	
	@Expose
	@ManyToOne
	private RobotGroup robotGroup;
	
	@Expose
	@ManyToOne
	private Source source;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="robot")
	private List<DocumentURL> urls = new ArrayList<>();
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="robot")
	private List<AppToken> tokens = new ArrayList<>();
	
	@Transient
	private Boolean selected = false;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public RobotGroup getRobotGroup() {
		return robotGroup;
	}

	public void setRobotGroup(RobotGroup robotGroup) {
		this.robotGroup = robotGroup;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public List<AppToken> getTokens() {
		return tokens;
	}

	public void setTokens(List<AppToken> tokens) {
		this.tokens = tokens;
	}

	public List<DocumentURL> getUrls() {
		return urls;
	}

	public void setUrls(List<DocumentURL> urls) {
		this.urls = urls;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Robot other = (Robot) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
}