package pl.gisexpert.cms.data.analysis;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import pl.gisexpert.cms.data.AbstractRepository;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class DemographicAnalysisRepository extends AbstractRepository<DemographicAnalysis> {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public DemographicAnalysisRepository() {
		super(DemographicAnalysis.class);
	}

}
