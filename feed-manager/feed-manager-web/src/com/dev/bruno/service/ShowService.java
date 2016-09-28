package com.dev.bruno.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import com.dev.bruno.dao.DocumentDAO;
import com.dev.bruno.dao.DocumentURLDAO;
import com.dev.bruno.dao.ShowDAO;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Document;
import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.model.Local;
import com.dev.bruno.model.Robot;
import com.dev.bruno.model.Show;
import com.dev.bruno.utils.HashUtils;

@Stateless
public class ShowService extends AbstractService {

	@Inject
	private ShowDAO showDAO;
	
	@Inject
	private DocumentDAO documentDAO;
	
	@Inject
	private DocumentURLDAO documentURLDAO;
	
	@Inject
	private LocalService localService;
	
	public Boolean exists(Long id) throws GenericException {
		return showDAO.exists(id);
	}

	public void aprovar(Show show) throws GenericException {

		if (showDAO.exists(show.getId())) {
			show.setAprovacao(new Date());
			show.setRejeicao(null);
			showDAO.update(show);
		}
	}

	public void rejeitar(Show show) throws GenericException {
		if (showDAO.exists(show.getId())) {
			show.setAprovacao(null);
			show.setRejeicao(new Date());
			showDAO.update(show);
		}
	}
	
	public void removeFromRobot(Long robotId) throws GenericException {
		showDAO.removeFromRobot(robotId);
	}
	
	public void add(Show show) throws GenericException {
		show.setId(null);
		
		validateShow(show);
		
		Local local = localService.add(show.getLocalStr());
		
		show.setLocal(local);
		
		DocumentURL documentURL = documentURLDAO.findByURL(show.getDocument().getBaseUrl());

		Document document = new Document();
		document.setKey(generateKey(show));
		document.setBaseUrl(show.getDocument().getBaseUrl());
		document.setNormalizationDate(new Date());
		document.setUrl(documentURL);
		documentDAO.add(document);
		
		show.setDocument(document);
		
		showDAO.add(show);
	}
	
	public Show get(Long id) throws GenericException {
		return showDAO.findById(id);
	}
	
	public List<Show> list(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return showDAO.list(queryStr, start, limit, order, dir);
	}
	
	public List<Show> listarShowsPendentes(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return showDAO.listarShowsPendentes(queryStr, start, limit, order, dir);
	}
	
	public Long contarNormalizados(Robot robot) throws GenericException {
		return showDAO.contarNormalizados(robot);
	}
	
	public Long contarNaoNormalizados(Robot robot) throws GenericException {
		return showDAO.contarNaoNormalizados(robot);
	}
	
	public Long contarAceitos(Robot robot) throws GenericException {
		return showDAO.contarAceitos(robot);
	}
	
	public Long contarNaoAceitos(Robot robot) throws GenericException {
		return showDAO.contarNaoAceitos(robot);
	}
	
	public Date ultimaCaptacao(Robot robot) throws GenericException {
		return showDAO.ultimaCaptacao(robot);
	}
	
	public Date ultimaNormalizacao(Robot robot) throws GenericException {
		return showDAO.ultimaNormalizacao(robot);
	}
	
	public List<Show> listarShowsPendentesFiltros(Date dataIniciorealizacao, Date dataFimRealizacao, String nome, String local) throws GenericException {
		return showDAO.listarShowsPendentesFiltros(dataIniciorealizacao, dataFimRealizacao, nome, local);
	}
	
	public Long getCount(String queryStr) throws GenericException {
		return showDAO.getCount(queryStr);
	}
	
	public void remove(Long id) throws GenericException {
		Show show = showDAO.findById(id);
		showDAO.remove(show);
	}
	
	public void validateShow(Show show) throws GenericException {
		if(show == null || (show.getId() != null && !documentDAO.exists(show.getId()))) {
			throw new EntityNotFoundException(documentDAO.getEntityName() + " não encontrado");
		}
		
		if(show.getLocalStr() == null || show.getNome() == null || show.getRealizacao() == null) {
			throw new MandatoryFieldsException("local, nome, realizacao são campos obrigatórios");
		}
	}
	
	private String generateKey(Show show) {
		String chave = show.getLocal().getNome() + "_" + show.getRealizacao().getTime();
		
		return HashUtils.getHash(chave.toLowerCase());
	}
}