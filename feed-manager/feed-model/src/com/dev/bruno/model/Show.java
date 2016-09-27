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
@Table(name="SHOW")
public class Show implements Serializable {

	private static final long serialVersionUID = 1763502662306669529L;

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_SHOW")
	protected Long id;
	
	@Expose
	@ManyToOne
	private Document document;
	
	@Expose
	@ManyToOne
	private Local local;
	
	@Expose
	@Column(name="DSC_NOME",nullable=false)
	private String nome;
	
	@Expose
	@Column(name="DSC_LOCAL",length=2000,nullable=false)
	private String localStr;
	
	@Expose
	@Column(name="DSC_SINOPSE", length=100000)
	private String sinopse;

	@Expose
	@Column(name="DAT_REALIZACAO",nullable=false)
	private Date realizacao;
	
	@Expose
	@Column(name="DAT_REJEICAO")
	private Date rejeicao;
	
	@Expose
	@Column(name="DAT_APROVACAO")
	private Date aprovacao;
	
	@Transient
	private Boolean accepted = false;
	
	@Transient
	private Boolean rejected = false;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLocalStr() {
		return localStr;
	}

	public void setLocalStr(String localStr) {
		this.localStr = localStr;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public Date getRealizacao() {
		return realizacao;
	}

	public void setRealizacao(Date realizacao) {
		this.realizacao = realizacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Date getRejeicao() {
		return rejeicao;
	}

	public void setRejeicao(Date rejeicao) {
		this.rejeicao = rejeicao;
	}

	public Date getAprovacao() {
		return aprovacao;
	}

	public void setAprovacao(Date aprovacao) {
		this.aprovacao = aprovacao;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public Boolean getRejected() {
		return rejected;
	}

	public void setRejected(Boolean rejected) {
		this.rejected = rejected;
	}

	public String getKey() {
//		String chave = getLocal().getNome() + "_" + getRealizacao().getTime();
//		return HashUtils.getHash(chave);
		return document.getKey();
	}
}