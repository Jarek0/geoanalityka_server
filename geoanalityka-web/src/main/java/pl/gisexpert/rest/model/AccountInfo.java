package pl.gisexpert.rest.model;

import java.util.Date;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Address;

public class AccountInfo {
	private String username;
	private String email;
	private Date lastLogin;
	private String companyName;
	private String firstName;
	private String lastName;
	private Double credits;
	private Date tokenExpires;
	private String accessToken;
	private Double queuedPayment;
	
	public AccountInfo(){
		
	}
	
	public AccountInfo(Account account) {
		
		username = account.getUsername();
		email=account.getEmailAddress();
		lastLogin = account.getLastLoginDate();
		credits = account.getCredits();
		queuedPayment = account.getQueuedPayment();
		
		Address address = account.getAddress();
		firstName = address.getFirstName();
		lastName = address.getLastName();
		companyName = address.getCompanyName();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Double getCredits() {
		return credits;
	}
	public void setCredits(Double credits) {
		this.credits = credits;
	}
	public Date getTokenExpires() {
		return tokenExpires;
	}
	public void setTokenExpires(Date tokenExpires) {
		this.tokenExpires = tokenExpires;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Double getQueuedPayment() {
		return queuedPayment;
	}
	public void setQueuedPayment(Double queuedPayment) {
		this.queuedPayment = queuedPayment;
	}
}
