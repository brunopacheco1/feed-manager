package com.dev.bruno.resource;

import javax.interceptor.Interceptors;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dev.bruno.interceptor.RequestTimeInterceptor;
import com.dev.bruno.utils.GsonUtils;
import com.google.gson.Gson;

@Produces(MediaType.APPLICATION_JSON)
@Interceptors(RequestTimeInterceptor.class)
public abstract class AbstractResource {

	protected Gson gsonWithExclusion = GsonUtils.getGsonWithExclusion();
	
	protected Gson gson = GsonUtils.getGson();
}