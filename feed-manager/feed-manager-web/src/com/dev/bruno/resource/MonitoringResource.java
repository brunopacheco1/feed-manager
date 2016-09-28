package com.dev.bruno.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.Monitoring;
import com.dev.bruno.service.MonitoringService;

@Stateless
@Path("monitoring")
public class MonitoringResource extends AbstractResource {

	@Inject
	private MonitoringService monitoringService;
	
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") Long id) throws GenericException {
		Monitoring monitoring = monitoringService.get(id);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(monitoring)).build();
	}
	
	@GET
	public Response list(@QueryParam("query") String query, @DefaultValue("0") @QueryParam("start") Integer start, @DefaultValue("100") @QueryParam("limit") Integer limit, @DefaultValue("id") @QueryParam("order") String order, @DefaultValue("asc") @QueryParam("dir") String dir) throws GenericException {
		Map<String, Object> result = new HashMap<>();
		
		List<Monitoring> monitorings = monitoringService.list(query, start, limit, order, dir);
		
		result.put("resultSize", monitorings.size());
		result.put("totalSize", monitoringService.getCount(query));
		result.put("result", monitorings);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(result)).build();
	}
}