package com.dev.bruno.response;

public class ExecutionResponse extends GenericResponse {

	private ExecutionStatus status;
	
	private String robot;
	
	public ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutionStatus status) {
		this.status = status;
		this.setMessage(status.getMsg());
	}
	
	public String getRobot() {
		return robot;
	}

	public void setRobot(String robot) {
		this.robot = robot;
	}
}