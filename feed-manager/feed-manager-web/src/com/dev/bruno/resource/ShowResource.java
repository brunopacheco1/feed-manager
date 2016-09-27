package com.dev.bruno.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.Show;
import com.dev.bruno.resource.AbstractResource;
import com.dev.bruno.service.ShowService;

@Stateless
@Path("show")
public class ShowResource extends AbstractResource {

	@EJB
	private ShowService showService;
	
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") Long id) throws GenericException {
		Show show = showService.get(id);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(show)).build();
	}
	
	@GET
	public Response list(@QueryParam("query") String query, @DefaultValue("0") @QueryParam("start") Integer start, @DefaultValue("100") @QueryParam("limit") Integer limit, @DefaultValue("id") @QueryParam("order") String order, @DefaultValue("asc") @QueryParam("dir") String dir) throws GenericException {
		Map<String, Object> result = new HashMap<>();
		
		List<Show> shows = showService.list(query, start, limit, order, dir);
		
		result.put("resultSize", shows.size());
		result.put("totalSize", showService.getCount(query));
		result.put("result", shows);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(result)).build();
	}
}