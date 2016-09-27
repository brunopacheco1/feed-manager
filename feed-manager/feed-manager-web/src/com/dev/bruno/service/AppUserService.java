package com.dev.bruno.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dev.bruno.dao.AppUserDAO;
import com.dev.bruno.exception.EntityExistsException;
import com.dev.bruno.exception.EntityNotFoundException;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.InvalidAccessException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.AppToken;
import com.dev.bruno.model.AppUser;
import com.dev.bruno.model.AppUserGroup;
import com.dev.bruno.service.AbstractService;

@Stateless
public class AppUserService extends AbstractService {

	@EJB
	private AppUserDAO userDAO;
	
	@EJB
	private AppUserGroupService groupService;

	@EJB
	private AppTokenService tokenService;

	public AppUser add(AppUser user) throws GenericException {
		user.setId(null);

		validateUser(user);
		
		AppUserGroup group = groupService.get(user.getUserGroup().getId());
		
		user.setPassword(hashPassword(user.getPassword()));
		user.setUserGroup(group);
		
		userDAO.add(user);
		
		return user;
	}

	public AppUser update(Long id, AppUser user) throws GenericException {
		user.setId(id);

		validateUser(user);

		AppUserGroup group = groupService.get(user.getUserGroup().getId());
		
		user.setUserGroup(group);
		
		userDAO.update(user);

		return user;
	}

	public AppUser get(Long id) throws GenericException {
		return userDAO.findById(id);
	}

	public List<AppUser> list(String queryStr, Integer start, Integer limit, String order, String dir) throws GenericException {
		return userDAO.list(queryStr, start, limit, order, dir);
	}

	public Long getCount(String queryStr) {
		return userDAO.getCount(queryStr);
	}

	public void remove(Long id) throws GenericException {
		AppUser user = userDAO.findById(id);
		userDAO.remove(user);
	}

	public AppToken login(AppUser userLogin) throws GenericException {
		if (userLogin == null || userLogin.getLogin() == null || userLogin.getPassword() == null) {
			throw new MandatoryFieldsException("login e password são campos obrigatórios");
		}

		AppUser user = userDAO.findByLogin(userLogin.getLogin());

		if (user == null) {
			throw new EntityNotFoundException(userDAO.getEntityName() + " não encontrado");
		}

		if (!hashPassword(userLogin.getPassword()).equals(user.getPassword())) {
			throw new InvalidAccessException("Senha informada não confere");
		}

		return tokenService.generateToken(user);
	}

	public void validateUser(AppUser user) throws GenericException {
		if (user == null || (user.getId() != null && !userDAO.exists(user.getId()))) {
			throw new EntityNotFoundException(userDAO.getEntityName() + " não encontrado");
		}

		if (user.getUserGroup() == null || user.getUserGroup().getId() == null || user.getLogin() == null || user.getName() == null || user.getPassword() == null) {
			throw new MandatoryFieldsException("userGroup.id, login, name e password são campos obrigatórios");
		}

		if (userDAO.existsByLogin(user.getId(), user.getLogin())) {
			throw new EntityExistsException("Já existe um " + userDAO.getEntityName() + " cadastrado com esse login");
		}
	}

	private String hashPassword(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		if(md == null || password == null) {
			return password;
		}

		md.update(password.getBytes());

		byte[] mdbytes = md.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			String hex = Integer.toHexString(0xff & mdbytes[i]);
			
			if (hex.length() == 1) {
				hexString.append('0');
			}
			
			hexString.append(hex);
		}
		
		return hexString.toString();
	}

	public Boolean existsByLogin(Long id, String login) throws GenericException {
		return userDAO.existsByLogin(id, login);
	}

	public AppUser findByLogin(String login) throws GenericException {
		return userDAO.findByLogin(login);
	}
}