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
import com.dev.bruno.model.Source;
import com.dev.bruno.resource.AbstractResource;
import com.dev.bruno.service.SourceService;

@Stateless
@Path("source")
public class SourceResource extends AbstractResource {

	@EJB
	private SourceService sourceService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Source source) throws GenericException {
		sourceService.add(source);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(source)).build();
	}
	
	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") Long id, Source source) throws GenericException {
		sourceService.update(id, source);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(source)).build();
	}
	
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") Long id) throws GenericException {
		Source source = sourceService.get(id);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(source)).build();
	}
	
	@GET
	public Response list(@QueryParam("query") String query, @DefaultValue("0") @QueryParam("start") Integer start, @DefaultValue("100") @QueryParam("limit") Integer limit, @DefaultValue("id") @QueryParam("order") String order, @DefaultValue("asc") @QueryParam("dir") String dir) throws GenericException {
		Map<String, Object> result = new HashMap<>();
		
		List<Source> sources = sourceService.list(query, start, limit, order, dir);
		
		result.put("resultSize", sources.size());
		result.put("totalSize", sourceService.getCount(query));
		result.put("result", sources);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(result)).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") Long id) throws GenericException {
		sourceService.remove(id);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("removed", true);
		
		return Response.status(Status.OK).entity(response).build();
	}
}