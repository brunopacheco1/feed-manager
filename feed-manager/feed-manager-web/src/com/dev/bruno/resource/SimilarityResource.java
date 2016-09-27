package com.dev.bruno.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.quartz.SchedulerException;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.resource.AbstractResource;
import com.dev.bruno.response.GenericResponse;
import com.dev.bruno.service.SchedulerService;
import com.dev.bruno.service.SimilarityService;

@Stateless
@Path("similarity")
public class SimilarityResource extends AbstractResource {

	@EJB
	private SimilarityService similarityService;
	
	@EJB
	private SchedulerService schedulerService;
	
	@GET
	public Response list(@DefaultValue("0.9") @QueryParam("percentage") Double percentage) throws GenericException {
		Map<String, Object> result = new HashMap<>();
		
		List<String> locais = similarityService.checkSimilarity(percentage);
		
		result.put("percentage", percentage);
		result.put("totalSize", locais.size());
		result.put("result", locais);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(result)).build();
	}
	
	@POST
	public Response correctSimilarity(List<String> expressions) throws GenericException, SchedulerException {
		schedulerService.scheduleCorrection(expressions);
		
		GenericResponse response = new GenericResponse();
		response.setMessage("Correção agendada com sucesso!");
		
		return Response.status(Status.OK).entity(response).build();
	}
}