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
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class AccountInfo {

	private String username;
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

	public AccountInfo(Account account, List<Role> roles) {
		lastLogin = account.getLastLoginDate();
		firstName = account.getFirstName();
		lastName = account.getLastName();
		accountType = account.getDiscriminatorValue();

		this.roles = Lists.transform(roles, Role::getName);
	}
}
