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
@Table(name="GRUPO_ROBO")
public class RobotGroup implements Serializable {

	private static final long serialVersionUID = 3132944348997302263L;
	
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	@Column(name="COD_GRUPO_ROBO")
	private Long id;
	
	@Expose
	@Column(name="DSC_PADRAO_CRON",nullable=false,length=1000)
	private String cronPattern;
	
	@Expose
	@Column(name="DSC_NOME",nullable=false,length=1000)
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="robotGroup")
	private List<Robot> robots = new ArrayList<>();
	
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

	public String getCronPattern() {
		return cronPattern;
	}

	public void setCronPattern(String cronPattern) {
		this.cronPattern = cronPattern;
	}

	public List<Robot> getRobots() {
		return robots;
	}

	public void setRobots(List<Robot> robots) {
		this.robots = robots;
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
		RobotGroup other = (RobotGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}