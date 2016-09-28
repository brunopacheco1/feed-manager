package com.dev.bruno.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

import org.quartz.SchedulerException;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.AppToken;
import com.dev.bruno.model.AppUser;
import com.dev.bruno.service.AppTokenService;
import com.dev.bruno.service.AppUserService;

@Stateless
@Path("user")
public class AppUserResource extends AbstractResource {

	@Inject
	private AppUserService userService;
	
	@Inject
	private AppTokenService tokenService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(AppUser user) throws GenericException, SchedulerException {
		userService.add(user);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(user)).build();
	}
	
	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") Long id, AppUser user) throws GenericException, SchedulerException {
		userService.update(id, user);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(user)).build();
	}
	
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") Long id) throws GenericException {
		AppUser user = userService.get(id);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(user)).build();
	}
	
	@GET
	public Response list(@QueryParam("query") String query, @DefaultValue("0") @QueryParam("start") Integer start, @DefaultValue("100") @QueryParam("limit") Integer limit, @DefaultValue("id") @QueryParam("order") String order, @DefaultValue("asc") @QueryParam("dir") String dir) throws GenericException {
		Map<String, Object> result = new HashMap<>();
		
		List<AppUser> users = userService.list(query, start, limit, order, dir);
		
		result.put("resultSize", users.size());
		result.put("totalSize", userService.getCount(query));
		result.put("result", users);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(result)).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") Long id) throws GenericException {
		userService.remove(id);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("removed", true);
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	@POST
	@Path("/login")
	public Response login(AppUser user) throws GenericException {
		AppToken token = userService.login(user);
		
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(token)).build();
	}
	
	@POST
	@Path("/logout")
	public Response logout(AppToken token) throws GenericException {
		tokenService.discardToken(token);
		return Response.status(Status.OK).entity(gsonWithExclusion.toJson(token)).build();
	}
}