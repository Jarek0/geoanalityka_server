package pl.gisexpert.payu.model;

import pl.gisexpert.cms.model.Account;

public class Buyer {

	private String email;
	private String phone;
	private String firstName;
	private String lastName;
	private String language;
	private String customerId;
	
	public Buyer(){
		
	}
	
	public Buyer(Account account) {
		this.email = account.getEmailAddress();
		this.phone = account.getPhone();
		this.firstName = account.getFirstName();
		this.lastName = account.getLastName();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
