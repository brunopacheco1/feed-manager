package com.dev.bruno.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.Robot;
import com.dev.bruno.resource.AbstractResource;
import com.dev.bruno.service.CrawlingService;
import com.dev.bruno.service.RobotService;

@Stateless
@Path("robot")
public class RobotResource extends AbstractResource {

	@EJB
	private RobotService robotService;
	
	@EJB
	private CrawlingService crawlingService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Robot robot) throws GenericException {
		robotService.add(robot);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(robot)).build();
	}

	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") Long id, Robot robot) throws GenericException {
		robotService.update(id, robot);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(robot)).build();
	}

	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") Long id) throws GenericException {
		Robot robot = robotService.get(id);
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(robot)).build();
	}

	@GET
	public Response list(@QueryParam("query") String query, @DefaultValue("0") @QueryParam("start") Integer start, @DefaultValue("100") @QueryParam("limit") Integer limit, @DefaultValue("id") @QueryParam("order") String order, @DefaultValue("asc") @QueryParam("dir") String dir) throws GenericException {
		Map<String, Object> result = new HashMap<>();
		
		List<Robot> robots = robotService.list(query, start, limit, order, dir);

		result.put("resultSize", robots.size());
		result.put("totalSize", robotService.getCount(query));
		result.put("result", robots);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(result)).build();
	}

	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") Long id) throws GenericException {
		robotService.remove(id);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("removed", true);
		
		return Response.status(Status.OK).entity(response).build();
	}

	@POST
	@Path("/{id}/run")
	public Response run(@PathParam("id") Long id) throws GenericException {
		return Response.status(Status.OK).entity(crawlingService.runCrawler(id)).build();
	}

	@GET
	@Path("/{id}/status")
	public Response status(@PathParam("id") Long id) throws GenericException {
		return Response.status(Status.OK).entity(robotService.status(id)).build();
	}
}