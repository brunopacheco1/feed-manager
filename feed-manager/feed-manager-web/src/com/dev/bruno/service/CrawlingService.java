package com.dev.bruno.service;

import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.dev.bruno.dto.CrawlerDTO;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.exception.RobotAccessException;
import com.dev.bruno.model.AppToken;
import com.dev.bruno.model.Robot;
import com.dev.bruno.response.ExecutionResponse;
import com.dev.bruno.service.AbstractService;

@Stateless
public class CrawlingService extends AbstractService {
	
	@EJB
	private RobotService robotSerice;
	
	@EJB
	private DocumentURLService urlService;
	
	@EJB
	private AppTokenService tokenService;
	
	@Resource(name="api.address")
	private String apiAddress;

	public ExecutionResponse runCrawler(Long id) throws GenericException {
		Robot robot = new Robot();
		robot.setId(id);
		return runCrawler(robot);
	}
	
	public ExecutionResponse runCrawler(Robot robot) throws GenericException {
		robot = robotSerice.get(robot.getId());
		
		AppToken token = tokenService.generateToken(robot);
		
		CrawlerDTO robotDTO = new CrawlerDTO();
		robotDTO.setConnectionTimeout(robot.getConnectionTimeout());
		robotDTO.setDelay(robot.getDelay());
		robotDTO.setDocumentURLRegex(robot.getDocumentURLRegex());
		robotDTO.setEndDepth(robot.getEndDepth());
		robotDTO.setKey(token.getToken());
		robotDTO.setManagerUrl(apiAddress + "/document-url/" + robot.getId() + "/urls");
		robotDTO.setSourceURLRegex(robot.getSourceURLRegex());
		robotDTO.setURLs(robot.getSource().getUrls());
		
		Client client = ClientBuilder.newClient();
		try {
			return client.target(robot.getUrl() + "/robot/crawler/run").request().post(Entity.entity(gsonWithExclusion.toJson(robotDTO), MediaType.APPLICATION_JSON_TYPE), ExecutionResponse.class);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		throw new RobotAccessException(robot.getId());
	}
}