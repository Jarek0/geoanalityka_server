package pl.gisexpert.cms.model;

import javax.persistence.*;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import pl.gisexpert.cms.visitor.AccountVisitor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("natural_person")

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class NaturalPersonAccount extends Account {
	
	private static final long serialVersionUID = 9220917704905835016L;

	@Audited
	@AuditJoinTable(name = "account_addresses_aud")
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "address_id")
	private Address address;

	@Override
	public void accept(AccountVisitor visitor) {
		visitor.visit(this);
	}

	public NaturalPersonAccount(String username, String firstName, String lastName, String password, String phone,
								Set<Role> roles, Date dateRegistered, AccountStatus accountStatus, AccountConfirmation accountConfirmation,
								Address address) {
		super(username, firstName, lastName, password, phone, roles, dateRegistered, accountStatus, accountConfirmation);
		this.address = address;
	}
}
