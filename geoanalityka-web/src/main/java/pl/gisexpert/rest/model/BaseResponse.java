package pl.gisexpert.rest.model;

import javax.ws.rs.core.Response.Status;

public class BaseResponse {

	private Status responseStatus;
	private String message;
	
	public Status getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(Status responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
