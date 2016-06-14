package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.hibernate.validator.constraints.Email;

import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;

@Entity
@Table(name = "accounts", indexes = {@Index(name="username_index", columnList="username", unique=true)})
@NamedNativeQuery(name = "Account.removeRole", query = "DELETE FROM account_roles WHERE username = ? AND role = ?")
public class Account implements Serializable {

    private static final long serialVersionUID = 1033705321916453635L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    @NotNull
    @Size(min = 1, max = 30)
    private String username;

    @Column(nullable = false, length = 102)
    @NotNull
    private String password;

    @ManyToMany
    @JoinTable(name = "account_roles",
            joinColumns = {
                @JoinColumn(name = "username", referencedColumnName = "username")},
            inverseJoinColumns = {
                @JoinColumn(name = "role", referencedColumnName = "name")})
    private Collection<Role> roles;

    @Email
    @Column(name = "email_address", nullable = false, length = 80)
    @NotNull
    private String emailAddress;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false, name = "date_registered")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateRegistered;

    @Column(name = "date_last_login")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastLoginDate;
    
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;
    
    @Column(nullable = false)
    private Double credits = 0.0;

    @Embedded
    private ResetPassword resetPassword;
    
    @Embedded
    private AccountConfirmation accountConfirmation;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "account_tokens",
    joinColumns = {
        @JoinColumn(name = "account_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "token_id", referencedColumnName = "id")})
    private Collection<AccessToken> tokens;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_analyses",
    joinColumns = {
        @JoinColumn(name = "account_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "analysis_id", referencedColumnName = "id")})
    private Collection<DemographicAnalysis> analyses;
    
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
	
	public Collection<AccessToken> getTokens() {
		return tokens;
	}

	public void setTokens(Collection<AccessToken> tokens) {
		this.tokens = tokens;
	}

	public Double getCredits() {
		return credits;
	}

	public void setCredits(Double credits) {
		this.credits = credits;
	}	

	public Collection<DemographicAnalysis> getAnalyses() {
		return analyses;
	}

	public void setAnalyses(Collection<DemographicAnalysis> analyses) {
		this.analyses = analyses;
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
