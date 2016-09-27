package com.dev.bruno.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import com.google.gson.annotations.Expose;

@Entity
@Table(name="DOCUMENTO_URL")
public class DocumentURL implements Serializable {
	
	private static final long serialVersionUID = 5794749270548053811L;
	
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_DOCUMENTO_URL")
	protected Long id;
	
	@Expose
	@Column(name="DAT_CAPTACAO")
	private Date captureDate;
	
	@Expose
	@Column(name="DSC_URL", length=2000, nullable=false)
	private String url;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="url")
	private List<Document> documents = new ArrayList<>();
	
	@Expose
	@ManyToOne
	private Robot robot;

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

	public Date getCaptureDate() {
		return captureDate;
	}

	public void setCaptureDate(Date captureDate) {
		this.captureDate = captureDate;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}
}