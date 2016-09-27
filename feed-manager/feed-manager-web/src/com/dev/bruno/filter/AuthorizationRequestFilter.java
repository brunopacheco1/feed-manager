package com.dev.bruno.filter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.service.AppTokenService;

@Provider
@Priority(2)
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	private AppTokenService tokenService = ServiceLocator.getInstance().lookup(AppTokenService.class);
	
	@Override
	public void filter(ContainerRequestContext requestCtx) {
		String token = requestCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		try {
			List<String> regexs = new ArrayList<>();
			regexs.add("^\\/user\\/login$");
			regexs.add("^\\/user\\/logout$");
			
			Boolean validate = true;
			for(String regex : regexs) {
				if(requestCtx.getUriInfo().getPath().matches(regex)) {
					validate = false;
					break;
				}
			}
			
			if(validate) {
				tokenService.validateToken(token);
			}
		} catch (GenericException e) {
			throw new WebApplicationException(e, Response.Status.UNAUTHORIZED);
		}
	}
}