package pl.gisexpert.cms.visitor;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.NaturalPersonAccount;

public interface AccountVisitor {
	void visit(NaturalPersonAccount account);
	void visit(Account account);
}
