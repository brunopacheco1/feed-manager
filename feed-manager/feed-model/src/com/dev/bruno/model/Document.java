package com.dev.bruno.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

@Entity
@Table(name="DOCUMENTO")
public class Document implements Serializable {
	
	private static final long serialVersionUID = 5794749270548053811L;
	
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_DOCUMENTO")
	protected Long id;
	
	@Expose
	@Column(name="DAT_NORMALIZACAO",nullable=false)
	private Date normalizationDate;
	
	@Expose
	@Transient
	private String baseUrl;
	
	@Expose
	@Column(name="DSC_CHAVE",nullable=false, length=32)
	protected String key;
	
	@Expose
	@ManyToOne
	private DocumentURL url;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public Date getNormalizationDate() {
		return normalizationDate;
	}

	public void setNormalizationDate(Date normalizationDate) {
		this.normalizationDate = normalizationDate;
	}

	public DocumentURL getUrl() {
		return url;
	}

	public void setUrl(DocumentURL url) {
		this.url = url;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		Document other = (Document) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}