package com.dev.bruno.job;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.service.SimilarityService;

public class DocumentKeyReviser implements Job {
	
	protected Logger logger = Logger.getLogger(this.getClass().getName());

	private SimilarityService similarityService = ServiceLocator.getInstance().lookup(SimilarityService.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("CORREÇÃO DE SIMILARIDADE INICIADA!");
		if(similarityService == null) {
			return;
		}
		
		String expressionsStr = context.getJobDetail().getJobDataMap().getString("expressions");
		List<String> expressions = Arrays.asList(expressionsStr.split("_SEPARATOR_"));
		
		try {
			similarityService.correctSimilarity(expressions);
		} catch (GenericException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		logger.info("CORREÇÃO DE SIMILARIDADE FINALIZADA!");
	}
}