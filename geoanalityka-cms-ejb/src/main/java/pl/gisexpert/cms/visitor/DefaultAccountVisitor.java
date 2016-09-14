package pl.gisexpert.cms.visitor;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.CompanyAccount;
import pl.gisexpert.cms.model.NaturalPersonAccount;

public class DefaultAccountVisitor implements AccountVisitor {
	
	@Override
	public void visit(NaturalPersonAccount account) {}

	@Override
	public void visit(CompanyAccount account) {}

	@Override
	public void visit(Account account) {}
	
}
