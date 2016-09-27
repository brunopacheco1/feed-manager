package com.dev.bruno.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.dev.bruno.response.GenericResponse;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<GenericException> {

	@Override
	public Response toResponse(GenericException t) {
		GenericResponse response = new GenericResponse();
		response.setMessage(t.getMessage());
		
		return Response.status(t.getStatus()).entity(response).type(MediaType.APPLICATION_JSON).build();
	}
}