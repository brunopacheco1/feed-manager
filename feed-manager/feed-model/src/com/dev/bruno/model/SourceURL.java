package com.dev.bruno.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name="FONTE_URL")
public class SourceURL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 991251957463810817L;

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_FONTE_URL")
	private Long id;
	
	@Expose
	@Column(name="DSC_URL",nullable=false,length=1000)
	private String url;
	
	@ManyToOne
	private Source source;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}
}