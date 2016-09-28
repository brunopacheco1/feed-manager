package com.dev.bruno.service;

import java.util.Date;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.dev.bruno.dao.AppTokenDAO;
import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.model.AppToken;
import com.dev.bruno.model.AppUser;
import com.dev.bruno.model.AppUserGroup;

@Singleton
@Startup
public class AppInitializerService extends AbstractService {

	@Inject
	private AppUserService userService;
	
	@Inject
	private AppUserGroupService groupService;
	
	@Inject
	private AppTokenDAO tokenDAO;
	
	@Resource(name="app.context")
	private String appContext;
	
	@PostConstruct
	private void initializeApp() throws GenericException {
		ServiceLocator.getInstance().setAppContext(appContext);
		
		createDefaultUser();
	}
	
	private void createDefaultUser() throws GenericException {
		String login = "admin";
		String groupName = "admin";
		
		AppUser appUser = null;
		
		AppUserGroup group = null;
		
		if(groupService.existsByName(null, groupName)) {
			group = groupService.findByName(groupName);
		} else {
			group = new AppUserGroup();
			group.setName(groupName);
			
			groupService.add(group);
		}
		
		if(userService.existsByLogin(null, login)) {
			appUser = userService.findByLogin(login);
		} else if(appUser == null) {
			appUser = new AppUser();
			appUser.setLogin(login);
			appUser.setName(login);
			appUser.setPassword(login);
			appUser.setUserGroup(group);
			
			try {
				appUser = userService.add(appUser);
			} catch (EntityExistsException e) {
				//Ignorar quando existir
			} catch (GenericException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
		String tokenStr = "b1f650401989debdf1bd9455b9fdbada";
		
		if(tokenDAO.exists(tokenStr)) {
			return;
		}
		
		AppToken token = new AppToken();

		token.setGenerationDate(new Date());
		token.setAppUser(appUser);
		token.setDuration(null);
		token.setToken(tokenStr);
		
		tokenDAO.add(token);
	}
}