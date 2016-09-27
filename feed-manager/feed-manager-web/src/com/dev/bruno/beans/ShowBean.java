package com.dev.bruno.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.model.Show;
import com.dev.bruno.service.ShowService;

@Name("showBean")
@Scope(ScopeType.CONVERSATION)
public class ShowBean extends AbstractBean {
	
	private static final long serialVersionUID = 4711587576971369635L;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	private List<ShowHolder> showHolders;
	private Map<String, ShowHolder> showHoldersMap;
	private List<Show> shows;
	private ShowService showService = ServiceLocator.getInstance().lookup(ShowService.class);
	private String indexInformation; 
	private Date datTelaRealizacaoInicial;
	private Date datTelaRealizacaoFinal;
	private String nomeShow;
	private String localShow;
	

	public List<Show> getShows() {
		return shows;
	}
	
	public void limparCamposTela(){
		nomeShow = "";
		localShow="";
		shows =  new ArrayList<>();
		datTelaRealizacaoInicial = null;
		datTelaRealizacaoFinal = null;
		showHolders = new ArrayList<ShowHolder>();
		showHoldersMap = new HashMap<>();
	}
	
	public void pesquisarShow() {
		shows = new ArrayList<>();

		try {
			if(!checkDate(datTelaRealizacaoInicial, datTelaRealizacaoFinal))
			return;
			
			shows.addAll(showService.listarShowsPendentesFiltros(datTelaRealizacaoInicial, datTelaRealizacaoFinal, nomeShow, localShow ));
			
			construirListaShow(shows);
		} catch (GenericException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		if(shows.isEmpty()) {
			addInfoMessageInfo("Nenhum resultado foi encontrado!");
		}	
	}

	public void construirListaShow(List<Show> shows) {
		boolean notHaving = true;

		showHolders = new ArrayList<>();
		showHoldersMap = new HashMap<>();
		
		for (Show show : shows) {
			notHaving = true;
			for (ShowHolder holder : showHolders) {

				if (holder.getKeyShow().equals(show.getKey())) {
					holder.getShows().add(show);
					notHaving = false;
				}
			}

			if (notHaving) {
				ShowHolder holder = new ShowHolder(show.getKey(), show.getNome(), show.getRealizacao(), show);
				showHoldersMap.put(show.getKey(), holder);
				showHolders.add(holder);
			}
		}
	}
	
	public void rejeitarShow(Show show) {
		if(!show.getRejected()) {
			return;
		}
		
		show.setAccepted(!show.getRejected());
	}
	
	public void aceitarShow(Show show) {
		if(!show.getAccepted()) {
			return;
		}
		
		show.setRejected(!show.getAccepted());
		
		for(ShowHolder holder : this.showHolders) {
			if(holder.getKeyShow().equals(show.getKey())) {
				for(Show child : holder.getShows()) {
					//REJEITAR DEMAIS SHOWS DO GRUPO QUE NAO ESTIVEREM MARCADOS COMO ACEITOS
					if(!child.getAccepted()) {
						child.setRejected(true);
					}
				}
				break;
			}
		}
	}
	
	public void confirmarAcao() throws GenericException {

		boolean hasAction = false;
		for (Show show : shows) {

			if (show.getAccepted()) {
				hasAction = true;
				showService.aprovar(show);
			} else if (show.getRejected()) {
				hasAction = true;
				showService.rejeitar(show);
			}
		}
		
		if(hasAction) {
			addInfoMessageInfo("Ação executada com sucesso!");
		} else {
			addInfoMessageInfo("Selecione algum show!");
		}
		
		pesquisarShow();
		
	}
	
	public List<ShowHolder> getShowHolders() {
		return showHolders;
	}

	public void setShowHolders(List<ShowHolder> showHolders) {
		this.showHolders = showHolders;
	}

	public String getIndexInformation() {
		return indexInformation;
	}

	public void setIndexInformation(String indexInformation) {
		this.indexInformation = indexInformation;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}

	public Date getDatTelaRealizacaoInicial() {
		return datTelaRealizacaoInicial;
	}

	public void setDatTelaRealizacaoInicial(Date datTelaRealizacaoInicial) {
		this.datTelaRealizacaoInicial = datTelaRealizacaoInicial;
	}

	public Date getDatTelaRealizacaoFinal() {
		return datTelaRealizacaoFinal;
	}

	public void setDatTelaRealizacaoFinal(Date datTelaRealizacaoFinal) {
		this.datTelaRealizacaoFinal = datTelaRealizacaoFinal;
	}

	public String getNomeShow() {
		return nomeShow;
	}

	public void setNomeShow(String nomeShow) {
		this.nomeShow = nomeShow;
	}

	public String getLocalShow() {
		return localShow;
	}

	public void setLocalShow(String localShow) {
		this.localShow = localShow;
	}
	
}
