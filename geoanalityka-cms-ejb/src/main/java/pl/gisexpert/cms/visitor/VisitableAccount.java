package pl.gisexpert.cms.visitor;

public interface VisitableAccount {
	public void accept(AccountVisitor visitor);
}
