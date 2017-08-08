package pl.gisexpert.cms.visitor;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.NaturalPersonAccount;

public interface AccountVisitor {
	public void visit(NaturalPersonAccount account);
	public void visit(Account account);
}
