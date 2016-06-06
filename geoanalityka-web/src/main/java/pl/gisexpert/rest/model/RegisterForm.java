package pl.gisexpert.rest.model;

public class RegisterForm extends BaseForm  {

	private String firstname;
	private String lastname;
	private String password;
	private String confirmPassword;
	private String email;
	
	private CompanyAddressForm companyAddress;

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

	public CompanyAddressForm getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(CompanyAddressForm companyAddress) {
		this.companyAddress = companyAddress;
	}

	
}
