package com.dev.bruno.service;

import java.util.Date;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.joda.time.DateTime;

import com.dev.bruno.dao.AppTokenDAO;
import com.dev.bruno.exception.EntityNotFoundException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.InvalidAccessException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.AppToken;
import com.dev.bruno.model.AppUser;
import com.dev.bruno.model.Robot;
import com.dev.bruno.service.AbstractService;

@Stateless
public class AppTokenService extends AbstractService {

	@EJB
	private AppTokenDAO tokenDAO;
	
	@EJB
	private AppUserService userService;
	
	@EJB
	private RobotService robotService;

	public AppToken generateToken(AppUser user) throws GenericException {
		userService.validateUser(user);
		
		AppToken token = new AppToken();

		token.setGenerationDate(new Date());
		token.setAppUser(user);
		token.setDuration(30);
		
		String tokenStr = null;
		
		while(tokenStr == null) {
			tokenStr = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
			
			if(tokenDAO.exists(tokenStr)) {
				token = null;
			}
		}
		
		token.setToken(tokenStr);
		
		tokenDAO.add(token);
		
		return token;
	}
	
	public AppToken generateToken(Robot robot) throws GenericException {
		robotService.validateRobot(robot);
		
		AppToken token = new AppToken();

		token.setGenerationDate(new Date());
		token.setRobot(robot);
		token.setDuration(120);
		String tokenStr = null;
		
		while(tokenStr == null) {
			tokenStr = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
			
			if(tokenDAO.exists(tokenStr)) {
				token = null;
			}
		}
		
		token.setToken(tokenStr);
		
		tokenDAO.add(token);
		
		return token;
	}

	public void validateToken(String tokenStr) throws GenericException {
		if(!isValidToken(tokenStr)) {
			throw new InvalidAccessException("Acesso negado, " + tokenDAO.getEntityName() + " inválido");
		}
	}
	
	public Boolean isValidToken(String tokenStr) throws GenericException {
		if (tokenStr == null || !tokenDAO.exists(tokenStr)) {
			throw new EntityNotFoundException(tokenDAO.getEntityName() + " não encontrado");
		}
		
		AppToken token = tokenDAO.findByToken(tokenStr);
		
		if(token.getDuration() == null) {
			return true;
		}
		
		DateTime endDate = new DateTime(token.getGenerationDate());
		
		endDate = endDate.plusMinutes(token.getDuration());
		
		if(endDate.isBeforeNow()) {
			return false;
		}
		
		return true;
	}
	
	public void discardToken(AppToken appToken) throws GenericException {
		if (appToken == null || appToken.getToken() == null || !tokenDAO.exists(appToken.getToken())) {
			throw new EntityNotFoundException(tokenDAO.getEntityName() + " não encontrado");
		}
		
		discardToken(appToken.getToken());
	}
	
	public void discardToken(String tokenStr) throws EntityNotFoundException, MandatoryFieldsException {
		AppToken token = tokenDAO.findByToken(tokenStr);
		
		if(token == null) {
			throw new EntityNotFoundException(tokenDAO.getEntityName() + " não encontrado");
		}
		
		tokenDAO.remove(token);
	}
}