package com.dev.bruno.service;

import java.util.logging.Logger;

import com.dev.bruno.utils.GsonUtils;
import com.google.gson.Gson;

public abstract class AbstractService {

	protected Gson gsonWithExclusion = GsonUtils.getGsonWithExclusion();
	
	protected Gson gsonWithoutExclusion = GsonUtils.getGson();

	protected Logger logger = Logger.getLogger(this.getClass().getName());
}