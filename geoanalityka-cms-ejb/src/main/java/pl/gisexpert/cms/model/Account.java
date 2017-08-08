package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

import pl.gisexpert.cms.visitor.AccountVisitor;
import pl.gisexpert.cms.visitor.VisitableAccount;

@Entity
@Table(name = "accounts", indexes = { @Index(name = "username_index", columnList = "username", unique = true) })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "Account.sumMapping", columns = {
		@ColumnResult(name = "items_count", type = Integer.class) }) })
@NamedNativeQueries({
		@NamedNativeQuery(name = "Account.removeRole", query = "DELETE FROM account_roles WHERE username = ? AND role = ?"),
		@NamedNativeQuery(name = "Account.getRoles", query = "SELECT roles.* FROM roles, account_roles WHERE account_roles.username = :username AND roles.name = account_roles.role", resultClass = Role.class),
		@NamedNativeQuery(name = "Account.hasRole", query = "SELECT COUNT(*) as items_count FROM account_roles WHERE username = :username AND role = :role", resultSetMapping = "Account.sumMapping") })
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode(of = {"emailAddress"})
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class Account implements Serializable, VisitableAccount {

	private static final long serialVersionUID = 1033705321916453635L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false, length = 80)
	@NotNull
	@Size(min = 1, max = 80)
	private String username;

	@Column(name = "first_name", nullable = false, length = 30)
	@NotNull
	@Size(min = 1, max = 30)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 30)
	@NotNull
	@Size(min = 1, max = 30)
	private String lastName;

	@Email
	@Column(name = "email_address", nullable = false, length = 80)
	@NotNull
	private String emailAddress;

	@Column(nullable = false, length = 102)
	@NotNull
	private String password;

	@Column(length = 18)
	private String phone;

	@Audited
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "account_roles", joinColumns = {
			@JoinColumn(name = "username", referencedColumnName = "username") }, inverseJoinColumns = {
					@JoinColumn(name = "role", referencedColumnName = "name") }, indexes = {
							@Index(name = "role_username_index", columnList = "username", unique = false) })
	private Set<Role> roles;

	@Column(nullable = false, name = "date_registered")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date dateRegistered;

	@Column(name = "date_last_login")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastLoginDate;

	@Column(name = "account_status", nullable = false)
	private AccountStatus accountStatus;

	@Embedded
	private ResetPassword resetPassword;

	@Embedded
	private AccountConfirmation accountConfirmation;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "account")
	private List<AccessToken> tokens;

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

	@Transient
	public String getDiscriminatorValue(){
	    DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );

	    return val == null ? null : val.value();
	}

	@Override
	public void accept(AccountVisitor visitor) {
		visitor.visit(this);		
	}

}
