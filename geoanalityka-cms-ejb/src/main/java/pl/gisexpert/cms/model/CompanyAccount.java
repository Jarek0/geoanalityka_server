package pl.gisexpert.cms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import pl.gisexpert.cms.visitor.AccountVisitor;

@Entity
@DiscriminatorValue("company")
public class CompanyAccount extends Account {

	private static final long serialVersionUID = 3939189394646767899L;

	@Audited
	@AuditJoinTable(name = "account_companies_aud")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@Override
	public void accept(AccountVisitor visitor) {
		visitor.visit(this);
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	
}
