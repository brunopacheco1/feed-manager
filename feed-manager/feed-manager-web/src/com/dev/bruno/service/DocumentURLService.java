package com.dev.bruno.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;

import com.dev.bruno.dao.DocumentURLDAO;
import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.model.Robot;
import com.dev.bruno.service.AbstractService;

@Stateless
public class DocumentURLService extends AbstractService {

	@EJB
	private DocumentURLDAO documentURLDAO;
	
	@EJB
	private RobotService robotService;
	
	public Boolean exists(Long id) throws GenericException {
		return documentURLDAO.exists(id);
	}
	
	public void add(DocumentURL url) throws GenericException {
		url.setId(null);
		
		validateDocumentURL(url);
		
		Robot robot = robotService.get(url.getRobot().getId());
		url.setRobot(robot);
		url.setCaptureDate(new Date());
		
		documentURLDAO.add(url);
	}
	
	public void update(Long id, DocumentURL url) throws GenericException {
		url.setId(id);
		
		validateDocumentURL(url);
		
		Robot robot = robotService.get(url.getRobot().getId());
		url.setRobot(robot);
		
		documentURLDAO.update(url);
	}
	
	public DocumentURL get(Long id) throws GenericException {
		return documentURLDAO.findById(id);
	}
	
	public List<DocumentURL> list(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return documentURLDAO.list(queryStr, start, limit, order, dir);
	}
	
	public Long getCount(String queryStr) throws GenericException {
		return documentURLDAO.getCount(queryStr);
	}
	
	public List<DocumentURL> list(Long robotId, Boolean normalized, Integer start, Integer limit, String order, String dir) throws GenericException {
		Robot robot = robotService.get(robotId);
		return documentURLDAO.list(robot, normalized, start, limit, order, dir);
	}
	
	public Long getCount(Long robotId, Boolean normalized) throws GenericException {
		Robot robot = robotService.get(robotId);
		return documentURLDAO.getCount(robot, normalized);
	}
	
	public void remove(Long id) throws GenericException {
		DocumentURL url = documentURLDAO.findById(id);
		documentURLDAO.remove(url);
	}
	
	public void validateDocumentURL(DocumentURL url) throws GenericException {
		if(url == null || (url.getId() != null && !documentURLDAO.exists(url.getId()))) {
			throw new EntityNotFoundException(documentURLDAO.getEntityName() + " não encontrado");
		}
		
		if(documentURLDAO.existsByUrl(url.getId(), url.getUrl())) {
			throw new EntityExistsException("Já existe um " + documentURLDAO.getEntityName() + " com esse url");
		}
		
		if(url.getRobot() == null || url.getRobot().getId() == null || url.getUrl() == null) {
			throw new MandatoryFieldsException("robot.id, url são campos obrigatórios");
		}
	}
	
	public List<DocumentURL> list(Robot robot, Boolean normalized, Integer start, Integer limit, String order, String dir) throws GenericException {
		return documentURLDAO.list(robot, normalized, start, limit, order, dir);
	}
	
	public Long getCount(Robot robot, Boolean normalized) throws GenericException {
		return documentURLDAO.getCount(robot, normalized);
	}
}