package com.dev.bruno.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dev.bruno.model.Show;

public class ShowHolder {
	
	private String kayShow;
	private String nome;
	private Date realização;
	private List<Show> shows;
	
	public ShowHolder(String kayShow, String nome, Date realização, Show show) {
		this.kayShow = kayShow;
		this.nome = nome;
		this.realização = realização;
		shows = new ArrayList<Show>();
		shows.add(show);
	}

	public String getKeyShow() {
		return kayShow;
	}

	public void setKayShow(String kayShow) {
		this.kayShow = kayShow;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Date getRealização() {
		return realização;
	}
	
	public void setRealização(Date realização) {
		this.realização = realização;
	}
	
	public List<Show> getShows() {
		return shows;
	}
	
	public void setShows(List<Show> shows) {
		this.shows = shows;
	}

}
