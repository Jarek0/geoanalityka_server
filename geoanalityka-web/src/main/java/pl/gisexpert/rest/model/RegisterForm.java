package pl.gisexpert.rest.model;

@lombok.Setter
@lombok.Getter
public class RegisterForm extends BaseForm  {

	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private String confirmPassword;
	private AddressForm address;
	private String captcha;
}
