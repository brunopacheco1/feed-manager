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

import com.google.gson.annotations.Expose;

@Entity
@Table(name="USUARIO_SISTEMA")
public class AppUser implements Serializable {

	private static final long serialVersionUID = 8595322840325307784L;

	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_USUARIO_SISTEMA")
	private Long id;
	
	@Expose
	@Column(name="DSC_NOME",nullable=false,length=1000)
	private String name;
	
	@Expose
	@Column(name="DSC_LOGIN",nullable=false,length=1000)
	private String login;
	
	@Column(name="DSC_SENHA",nullable=false,length=32)
	private String password;

	@Expose
	@ManyToOne
	private AppUserGroup userGroup;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="appUser")
	private List<AppToken> tokens = new ArrayList<>();
	
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<AppToken> getTokens() {
		return tokens;
	}

	public void setTokens(List<AppToken> tokens) {
		this.tokens = tokens;
	}

	public AppUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(AppUserGroup userGroup) {
		this.userGroup = userGroup;
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
		AppUser other = (AppUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}