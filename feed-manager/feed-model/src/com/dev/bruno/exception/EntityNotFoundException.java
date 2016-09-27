package com.dev.bruno.exception;


public class EntityNotFoundException extends GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6963057506068431542L;
	
	public EntityNotFoundException(String msg) {
		super(msg);
	}
}