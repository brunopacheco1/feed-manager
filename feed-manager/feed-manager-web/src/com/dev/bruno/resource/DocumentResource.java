package com.dev.bruno.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.Show;
import com.dev.bruno.queue.service.ShowQueueService;
import com.dev.bruno.resource.AbstractResource;

@Stateless
@Path("document")
public class DocumentResource extends AbstractResource {

	@EJB
	private ShowQueueService showQueueService;
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{robotId}/shows")
	public Response addShows(@PathParam("robotId") Long robotId, List<Show> shows) throws GenericException {
		showQueueService.add(robotId, shows);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("added", true);
		
		return Response.status(Status.OK).entity(response).build();
	}
}