package pl.gisexpert.cms.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Company;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@Stateless
public class CompanyService {

	@Inject
	@CMSEntityManager
	private EntityManager em;
	
	public Company findCompanyForAccount(Account account) {
		
		String queryString = "SELECT account.company FROM Account account WHERE account = :account";
		TypedQuery<Company> query = em.createQuery(queryString, Company.class);
		query.setParameter("account", account);

    	try {
    		Company company = query.getSingleResult();
    		return company;
    	} catch (Exception e) {
    		return null;
    	}
	}
	
}
