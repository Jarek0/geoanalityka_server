package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "accounts", indexes = { @Index(name = "username_index", columnList = "username", unique = true) })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "Account.sumMapping", columns = {
		@ColumnResult(name = "items_count", type = Integer.class) }) })
@NamedNativeQueries({
		@NamedNativeQuery(name = "Account.removeRole", query = "DELETE FROM account_roles WHERE username = ? AND role = ?"),
		@NamedNativeQuery(name = "Account.getRoles", query = "SELECT roles.* FROM roles, account_roles WHERE account_roles.username = :username AND roles.name = account_roles.role", resultClass = Role.class),
		@NamedNativeQuery(name = "Account.hasRole", query = "SELECT COUNT(*) as items_count FROM account_roles WHERE username = :username AND role = :role", resultSetMapping = "Account.sumMapping") })
public class Account implements Serializable {

	private static final long serialVersionUID = 1033705321916453635L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 80)
	@NotNull
	@Size(min = 1, max = 80)
	private String username;

	@Column(nullable = false, length = 102)
	@NotNull
	private String password;

	@Column(name = "first_name", nullable = false, length = 50)
	@NotNull
	@Size(min = 1, max = 50)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 30)
	@NotNull
	@Size(min = 1, max = 30)
	private String lastName;

	@Audited
	@ManyToMany
	@JoinTable(name = "account_roles", joinColumns = {
			@JoinColumn(name = "username", referencedColumnName = "username") }, inverseJoinColumns = {
					@JoinColumn(name = "role", referencedColumnName = "name") }, indexes = {
							@Index(name = "role_username_index", columnList = "username", unique = false) })
	private List<Role> roles;

	@Email
	@Column(name = "email_address", nullable = false, length = 80)
	@NotNull
	private String emailAddress;

	@Column(nullable = false, name = "date_registered")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date dateRegistered;

	@Column(name = "date_last_login")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastLoginDate;

	@Column(name = "account_status", nullable = false)
	private AccountStatus accountStatus;

	@Column(nullable = false)
	private Double credits = 500.0;

	@Column(name = "queued_payment")
	private Double queuedPayment;
	
	@Audited
	@AuditJoinTable(name = "account_companies_aud")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;
	
	@Audited
	@AuditJoinTable(name = "account_addresses_aud")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;
	
	@Column
	private Boolean naturalPerson;

	@Embedded
	private ResetPassword resetPassword;

	@Embedded
	private AccountConfirmation accountConfirmation;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "account")
	private List<AccessToken> tokens;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "buyer")
	private List<Order> orders;

	public String hashPassword(String password) {
		DefaultPasswordService passwordService = new DefaultPasswordService();
		DefaultHashService dhs = new DefaultHashService();
		dhs.setHashIterations(5);
		dhs.setHashAlgorithmName("SHA-256");
		dhs.setGeneratePublicSalt(true);
		dhs.setRandomNumberGenerator(new SecureRandomNumberGenerator());
		passwordService.setHashService(dhs);
		return passwordService.encryptPassword(password);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public ResetPassword getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(ResetPassword resetPassword) {
		this.resetPassword = resetPassword;
	}

	public AccountConfirmation getAccountConfirmation() {
		return accountConfirmation;
	}

	public void setAccountConfirmation(AccountConfirmation accountConfirmation) {
		this.accountConfirmation = accountConfirmation;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public List<AccessToken> getTokens() {
		return tokens;
	}

	public void setTokens(List<AccessToken> tokens) {
		this.tokens = tokens;
	}

	public Double getCredits() {
		return credits;
	}

	public void setCredits(Double credits) {
		this.credits = credits;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Double getQueuedPayment() {
		return queuedPayment;
	}

	public void setQueuedPayment(Double queuedPayment) {
		this.queuedPayment = queuedPayment;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}	

	public Boolean getNaturalPerson() {
		return naturalPerson;
	}

	public void setNaturalPerson(Boolean naturalPerson) {
		this.naturalPerson = naturalPerson;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + Objects.hashCode(this.username);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Account other = (Account) obj;
		if (!Objects.equals(this.username, other.username)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "pl.gisexpert.ejb.entity.Account[ username=" + username + " ]";
	}

}
