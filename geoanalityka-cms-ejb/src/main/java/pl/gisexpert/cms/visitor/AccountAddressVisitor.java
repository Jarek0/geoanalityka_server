package pl.gisexpert.cms.visitor;

import javax.inject.Inject;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Address;
import pl.gisexpert.cms.model.Company;
import pl.gisexpert.cms.model.CompanyAccount;
import pl.gisexpert.cms.model.NaturalPersonAccount;
import pl.gisexpert.cms.service.CompanyService;

public class AccountAddressVisitor implements AccountVisitor {

	@Inject
	CompanyService companyService;
	
	private Address address;
	private Company company;
	
	@Override
	public void visit(NaturalPersonAccount account) {
		this.address = account.getAddress();
	}

	@Override
	public void visit(CompanyAccount account) {
		this.company = companyService.findCompanyForAccount(account);
		this.address = this.company.getAddress();
	}

	@Override
	public void visit(Account account) {}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	
}
