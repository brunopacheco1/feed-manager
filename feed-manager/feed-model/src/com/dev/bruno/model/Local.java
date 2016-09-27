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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name="LOCAL")
public class Local implements Serializable {

	private static final long serialVersionUID = 1763502662306669529L;

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_LOCAL")
	private Long id;
	
	@Expose
	@Column(name="DSC_NOME",length=2000,nullable=false)
	private String nome;
	
	@Expose
	@OneToMany(cascade=CascadeType.ALL, mappedBy="local")
	private List<LocalSimilar> locaisSimilares = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<LocalSimilar> getLocaisSimilares() {
		return locaisSimilares;
	}

	public void setLocaisSimilares(List<LocalSimilar> locaisSimilares) {
		this.locaisSimilares = locaisSimilares;
	}
}