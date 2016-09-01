package pl.gisexpert.rest.model;

public class RegisterForm extends BaseForm  {

	private String firstname;
	private String lastname;
	private String password;
	private String confirmPassword;
	private String email;
	private Double queuedPayment;
	private Boolean naturalPerson;
	
	private AddressForm address;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AddressForm getAddress() {
		return address;
	}

	public void setAddress(AddressForm address) {
		this.address = address;
	}

	public Double getQueuedPayment() {
		return queuedPayment;
	}

	public void setQueuedPayment(Double queuedPayment) {
		this.queuedPayment = queuedPayment;
	}

	public Boolean getNaturalPerson() {
		return naturalPerson;
	}

	public void setNaturalPerson(Boolean naturalPerson) {
		this.naturalPerson = naturalPerson;
	}

	
}
