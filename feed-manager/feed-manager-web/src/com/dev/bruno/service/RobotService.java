package com.dev.bruno.service;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.dev.bruno.dao.RobotDAO;
import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.EntityNotFoundException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.exception.RobotAccessException;
import com.dev.bruno.model.Robot;
import com.dev.bruno.model.RobotGroup;
import com.dev.bruno.model.Source;
import com.dev.bruno.response.ExecutionResponse;

@Stateless
public class RobotService extends AbstractService {

	@Inject
	private RobotDAO robotDAO;
	
	@Inject
	private RobotGroupService groupService;
	
	@Inject
	private SourceService sourceService;
	
	@Inject
	private ShowService showService;
	
	@Resource(name="api.address")
	private String apiAddress;

	public Robot add(Robot robot) throws GenericException {
		robot.setId(null);
		
		validateRobot(robot);
		
		RobotGroup group = groupService.get(robot.getRobotGroup().getId());

		robot.setRobotGroup(group);

		Source source = sourceService.get(robot.getSource().getId());
		
		robot.setSource(source);
		
		robotDAO.add(robot);
		
		return robot;
	}

	public Robot update(Long id, Robot robot) throws GenericException {
		robot.setId(id);
		
		validateRobot(robot);
		
		RobotGroup group = groupService.get(robot.getRobotGroup().getId());

		robot.setRobotGroup(group);
		
		Source source = sourceService.get(robot.getSource().getId());
		
		robot.setSource(source);

		robotDAO.update(robot);
		
		return robot;
	}

	public Robot get(Long id) throws GenericException {
		return robotDAO.findById(id);
	}

	public List<Robot> list(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return robotDAO.list(queryStr, start, limit, order, dir);
	}
	
	public Long getCount(String queryStr) {
		return robotDAO.getCount(queryStr);
	}

	public void remove(Long id) throws GenericException {
		Robot robot = robotDAO.findById(id);
		robotDAO.remove(robot);
	}
	
	public void clearDocuments(Long id) throws GenericException {
		showService.removeFromRobot(id);
	}

	public ExecutionResponse status(Long id) throws GenericException {
		Robot robot = robotDAO.findById(id);
		return status(robot);
	}
	
	public ExecutionResponse status(Robot robot) throws GenericException {
		if(robot == null || !robotDAO.exists(robot.getId())) {
			throw new EntityNotFoundException(robotDAO.getEntityName() + " não encontrado");
		}
		
		Client client = ClientBuilder.newClient();
		try {
            return client.target(robot.getUrl() + "/robot/status").request().get(ExecutionResponse.class);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		throw new RobotAccessException(robot.getId());
	}
	
	public void validateRobot(Robot robot) throws GenericException {
		if(robot == null || (robot.getId() != null && !robotDAO.exists(robot.getId()))) {
			throw new EntityNotFoundException(robotDAO.getEntityName() + " não encontrado");
		}
		
		if(robotDAO.existsByUrl(robot.getId(), robot.getUrl())) {
			throw new EntityExistsException("Já existe um " + robotDAO.getEntityName() + " com esse url");
		}
		
		if(robot.getSource() == null || robot.getSource().getId() == null || robot.getRobotGroup() == null || robot.getRobotGroup().getId() == null || robot.getName() == null || robot.getUrl() == null || robot.getDocumentURLRegex() == null) {
			throw new MandatoryFieldsException("source.id, robotGroup.id, name, url, documentURLRegex são campos obrigatórios");
		}
	}
}