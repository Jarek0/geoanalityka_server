package pl.gisexpert.cms.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import pl.gisexpert.cms.model.Company;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class CompanyRepository extends AbstractRepository<Company> {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public CompanyRepository() {
		super(Company.class);
	}

}
