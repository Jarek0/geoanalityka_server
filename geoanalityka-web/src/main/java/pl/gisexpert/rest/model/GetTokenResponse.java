package pl.gisexpert.rest.model;

import java.util.Date;

public class GetTokenResponse extends BaseResponse {
	
	private String token;
	private Date expires;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}
	
	
}
