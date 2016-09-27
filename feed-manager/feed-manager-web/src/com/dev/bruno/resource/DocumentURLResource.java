package com.dev.bruno.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.DocumentURL;
import com.dev.bruno.queue.service.DocumentURLQueueService;
import com.dev.bruno.resource.AbstractResource;
import com.dev.bruno.service.DocumentURLService;

@Stateless
@Path("document-url")
public class DocumentURLResource extends AbstractResource {

	@EJB
	private DocumentURLQueueService documentURLQueueService;
	
	@EJB
	private DocumentURLService documentURLService;
	
	@POST
	@Path("/{robotId}/urls")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addURLs(@PathParam("robotId") Long robotId, List<DocumentURL> urls) throws GenericException {
		documentURLQueueService.add(robotId, urls);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("added", true);
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	@GET
	@Path("/{robotId}")
	public Response list(@PathParam("robotId") Long robotId, @DefaultValue("false") @QueryParam("normalized") Boolean normalized, @DefaultValue("0") @QueryParam("start") Integer start, @DefaultValue("100") @QueryParam("limit") Integer limit, @DefaultValue("id") @QueryParam("order") String order, @DefaultValue("asc") @QueryParam("dir") String dir) throws GenericException {
		Map<String, Object> result = new HashMap<>();
		
		List<DocumentURL> urls = documentURLService.list(robotId, normalized, start, limit, order, dir);

		result.put("resultSize", urls.size());
		result.put("totalSize", documentURLService.getCount(robotId, normalized));
		result.put("result", urls);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(result)).build();
	}
}