package pl.gisexpert.cms.visitor;

import javax.inject.Inject;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Address;
import pl.gisexpert.cms.model.NaturalPersonAccount;

public class AccountAddressVisitor implements AccountVisitor {

	private Address address;
	
	@Override
	public void visit(NaturalPersonAccount account) {
		this.address = account.getAddress();
	}

	@Override
	public void visit(Account account) {}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
