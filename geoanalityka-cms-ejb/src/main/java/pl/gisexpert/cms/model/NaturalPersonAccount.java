package pl.gisexpert.cms.model;

import javax.persistence.*;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import pl.gisexpert.cms.visitor.AccountVisitor;

@Entity
@DiscriminatorValue("natural_person")
public class NaturalPersonAccount extends Account {
	
	private static final long serialVersionUID = 9220917704905835016L;

	@Audited
	@AuditJoinTable(name = "account_addresses_aud")
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public void accept(AccountVisitor visitor) {
		visitor.visit(this);
	}
}
