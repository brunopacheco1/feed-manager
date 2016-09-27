package com.dev.bruno.exception;

public class RobotAccessException extends GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6868728188035678029L;

	public RobotAccessException(Long robotId) {
		super("Falha ao acessar o robô[" + robotId + "].");
	}
}