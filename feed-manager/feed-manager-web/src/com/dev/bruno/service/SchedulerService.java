package com.dev.bruno.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.job.DocumentKeyReviser;
import com.dev.bruno.job.RobotRunner;
import com.dev.bruno.model.RobotGroup;
import com.dev.bruno.service.AbstractService;

@Singleton
@Startup
public class SchedulerService extends AbstractService {
	
	private Scheduler scheduler;
	
	@EJB
	private RobotGroupService groupService;
	
	@PostConstruct
	private void scheduleAllJobs() throws SchedulerException, GenericException {
		scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	
    	Integer limit = 100;
    	Integer total = groupService.getCount(null).intValue();
    	Integer pages =  total / limit;
    	
    	if(total % limit != 0) {
    		pages++;
    	}
    	
    	for(Integer page = 0; page < pages; page++) {
	    	List<RobotGroup> groups = groupService.list(null, page * limit, limit, "id", "asc");
	    	
			for(RobotGroup group : groups) {
				schedule(group);
			}
    	}
	}
	
	public void schedule(RobotGroup group) throws SchedulerException, GenericException {
		groupService.validateGroup(group);
		
		JobDetail job = JobBuilder.newJob(RobotRunner.class).withIdentity(group.getName() + "-Job", group.getName() + "-Group").usingJobData("groupId", group.getId()).build();
		
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(group.getName() + "-Trigger", group.getName() + "-Group").withSchedule(CronScheduleBuilder.cronSchedule(group.getCronPattern())).build();
		
		if(scheduler.checkExists(trigger.getKey())) {
			scheduler.rescheduleJob(trigger.getKey(), trigger);
		} else {
			scheduler.scheduleJob(job, trigger);
		}
	}
	
	public void removeFromSchedule(RobotGroup group) throws SchedulerException {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(group.getName() + "-Trigger", group.getName() + "-Group").withSchedule(CronScheduleBuilder.cronSchedule(group.getCronPattern())).build();
		
		JobDetail job = JobBuilder.newJob(RobotRunner.class).withIdentity(group.getName() + "-Job", group.getName() + "-Group").usingJobData("groupId", group.getId()).build();
		
		if(scheduler.checkExists(trigger.getKey())) {
			scheduler.deleteJob(job.getKey());
		}
	}
	
	public void scheduleCorrection(List<String> expressions) throws SchedulerException {
		String expressionsStr = StringUtils.join(expressions, "_SEPARATOR_");
		
		JobDetail job = JobBuilder.newJob(DocumentKeyReviser.class).withIdentity("DocumentKeyReviser-Job", "DocumentKeyReviser-Group").usingJobData("expressions", expressionsStr).build();
		
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("DocumentKeyReviser-Trigger", "DocumentKeyReviser-Group").startNow().build();
		
		if(!scheduler.checkExists(trigger.getKey())) {
			scheduler.scheduleJob(job, trigger);
		}
	}
	
	@PreDestroy
	private void finish() throws SchedulerException {
		scheduler.shutdown(true);
	}
}