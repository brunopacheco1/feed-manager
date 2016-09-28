package com.dev.bruno.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.quartz.CronExpression;
import org.quartz.SchedulerException;

import com.dev.bruno.dao.RobotGroupDAO;
import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.EntityNotFoundException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.InvalidValueException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Robot;
import com.dev.bruno.model.RobotGroup;
import com.dev.bruno.response.ExecutionResponse;

@Stateless
public class RobotGroupService extends AbstractService {

	@Inject
	private RobotGroupDAO groupDAO;
	
	@Inject
	private RobotService robotService;
	
	@Inject
	private CrawlingService crawlingService;
	
	@Inject
	private NormalizationService normalizationService;
	
	@Inject
	private SchedulerService schedulerService;
	
	public RobotGroup add(RobotGroup group) throws GenericException, SchedulerException {
		group.setId(null);
		
		validateGroup(group);
		
		groupDAO.add(group);
		
		schedulerService.schedule(group);
		
		return group;
	}
	
	public RobotGroup update(Long id, RobotGroup group) throws GenericException, SchedulerException {
		group.setId(id);
		
		validateGroup(group);
		
		groupDAO.update(group);
		
		schedulerService.schedule(group);
		
		return group;
	}
	
	public RobotGroup get(Long id) throws GenericException {
		return groupDAO.findById(id);
	}
	
	public List<RobotGroup> list(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return groupDAO.list(queryStr, start, limit, order, dir);
	}
	
	public Long getCount(String queryStr) {
		return groupDAO.getCount(queryStr);
	}
	
	public void remove(Long id) throws GenericException, SchedulerException {
		RobotGroup group = groupDAO.findById(id);
		
		schedulerService.removeFromSchedule(group);
		
		groupDAO.remove(group);
	}
	
	public List<ExecutionResponse> runNormalizer(Long id) throws GenericException {
		RobotGroup group = groupDAO.findById(id);

		List<ExecutionResponse> responses = new ArrayList<>();

		for(Robot robot : group.getRobots()) {
			try {
				responses.add(normalizationService.runNormalizer(robot));
			} catch (GenericException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
		return responses;
	}
	
	public List<ExecutionResponse> runCrawler(Long id) throws GenericException {
		RobotGroup group = groupDAO.findById(id);

		List<ExecutionResponse> responses = new ArrayList<>();

		for(Robot robot : group.getRobots()) {
			try {
				responses.add(crawlingService.runCrawler(robot));
			} catch (GenericException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
		return responses;
	}
	
	public List<ExecutionResponse> status(Long id) throws GenericException {

		RobotGroup group = groupDAO.findById(id);
		List<ExecutionResponse> executionResponse = new ArrayList<>();

		for (Robot robot : group.getRobots()) {
			executionResponse.add(robotService.status(robot));
		}
		return executionResponse;
	}
	
	public void validateGroup(RobotGroup group) throws GenericException {
		if(group == null || (group.getId() != null && !groupDAO.exists(group.getId()))) {
			throw new EntityNotFoundException("Grupo de Robôs não encontrado");
		}
		
		if(group.getCronPattern() == null || group.getName() == null) {
			throw new MandatoryFieldsException("cronPattern e name são campos obrigatórios");
		}
		
		if(groupDAO.existsByName(group.getId(), group.getName())) {
			throw new EntityExistsException("Já existe um " + groupDAO.getEntityName() + " cadastrado com esse nome");
		}
		
		if(!CronExpression.isValidExpression(group.getCronPattern())) {
			throw new InvalidValueException("A expressão CRON está num formato inválido");
		}
	}
}