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
@Table(name="LOCAL_SIMILAR")
public class LocalSimilar implements Serializable {

	private static final long serialVersionUID = 1763502662306669529L;

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_LOCAL_SIMILAR")
	private Long id;
	
	@Expose
	@Column(name="DSC_NOME",nullable=false)
	private String nome;
	
	@ManyToOne
	private Local local;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}
}