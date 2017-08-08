package pl.gisexpert.rest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Role;

@lombok.Getter
@lombok.Setter
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
	private String accountType;
	private List<String> roles;
	private ArrayList<ArrayList<Double>> analysesBbox;

	public AccountInfo(){

	}
	
	public AccountInfo(Account account, List<Role> roles) {
		email=account.getEmailAddress();
		lastLogin = account.getLastLoginDate();
		firstName = account.getFirstName();
		lastName = account.getLastName();
		accountType = account.getDiscriminatorValue();

		this.roles = Lists.transform(roles, Role::getName);
	}
}
