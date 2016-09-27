package com.dev.bruno.job;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.service.RobotGroupService;

public class RobotRunner implements Job {
	
	protected Logger logger = Logger.getLogger(this.getClass().getName());

	private RobotGroupService groupService = ServiceLocator.getInstance().lookup(RobotGroupService.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if(groupService == null) {
			return;
		}
		
		Long groupId = context.getJobDetail().getJobDataMap().getLong("groupId");
		
		try {
			groupService.runCrawler(groupId);
		} catch (GenericException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
