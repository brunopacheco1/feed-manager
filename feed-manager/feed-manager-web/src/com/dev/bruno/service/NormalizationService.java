package com.dev.bruno.service;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.dev.bruno.dto.NormalizerDTO;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.NothingToDoException;
import com.dev.bruno.exception.RobotAccessException;
import com.dev.bruno.model.AppToken;
import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.model.Robot;
import com.dev.bruno.response.ExecutionResponse;
import com.dev.bruno.service.AbstractService;

@Stateless
public class NormalizationService extends AbstractService {
	
	@EJB
	private RobotService robotSerice;
	
	@EJB
	private DocumentURLService urlService;
	
	@EJB
	private AppTokenService tokenService;
	
	@Resource(name="api.address")
	private String apiAddress;

	public ExecutionResponse runNormalizer(Long id) throws GenericException {
		Robot robot = robotSerice.get(id);
		
		return runNormalizer(robot);
	}
	
	public ExecutionResponse runNormalizer(Robot robot) throws GenericException {
		if(urlService.getCount(robot, false) == 0) {
			throw new NothingToDoException("Não existem urls para normalizar");
		}
		
		List<DocumentURL> urls = urlService.list(robot, false, 0, 100, "id", "asc");
		
		AppToken token = tokenService.generateToken(robot);
		
		NormalizerDTO normalizerDTO = new NormalizerDTO();
		normalizerDTO.setUrls(urls);
		normalizerDTO.setDelay(robot.getDelay());
		normalizerDTO.setKey(token.getToken());
		normalizerDTO.setConnectionTimeout(robot.getConnectionTimeout());
		normalizerDTO.setManagerUrl(apiAddress + "/document/" + robot.getId());
		
		Client client = ClientBuilder.newClient();
		
		try {
			return client.target(robot.getUrl() + "/robot/normalizer/run").request().post(Entity.entity(gsonWithExclusion.toJson(normalizerDTO), MediaType.APPLICATION_JSON_TYPE), ExecutionResponse.class);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		throw new RobotAccessException(robot.getId());
	}
}