package pl.gisexpert.cms.visitor;


import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Address;
import pl.gisexpert.cms.model.NaturalPersonAccount;

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class AccountAddressVisitor implements AccountVisitor {

	private Address address;
	
	@Override
	public void visit(NaturalPersonAccount account) {
		this.address = account.getAddress();
	}

	@Override
	public void visit(Account account) {}
}
