package com.dev.bruno.api;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api")
public class BaseApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return Collections.emptySet();
	}
}