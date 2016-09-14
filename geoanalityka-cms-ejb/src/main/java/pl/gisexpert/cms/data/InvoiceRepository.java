package pl.gisexpert.cms.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import pl.gisexpert.cms.model.Invoice;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class InvoiceRepository extends AbstractRepository<Invoice> {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public InvoiceRepository() {
		super(Invoice.class);
	}

}
