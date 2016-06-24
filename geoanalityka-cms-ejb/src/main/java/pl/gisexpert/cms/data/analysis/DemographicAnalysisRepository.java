package pl.gisexpert.cms.data.analysis;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import pl.gisexpert.cms.data.AbstractRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.analysis.Analysis;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class DemographicAnalysisRepository extends AbstractRepository<Analysis> {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public DemographicAnalysisRepository() {
		super(Analysis.class);
	}
	
	@Transactional
	public DemographicAnalysis findByHash(UUID hash) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<DemographicAnalysis> cq = cb.createQuery(DemographicAnalysis.class);
		Root<DemographicAnalysis> analysis = cq.from(DemographicAnalysis.class);
		cq.select(analysis);
		cq.where(cb.equal(analysis.get("hash"), hash.toString()));
		TypedQuery<DemographicAnalysis> q = getEntityManager().createQuery(cq);

		try {
			DemographicAnalysis resultAnalysis = q.getSingleResult();
			return resultAnalysis;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Transactional
	public List<DemographicAnalysis> findMostRecentRangeForAccount(Account account, int begin, int end) {
		
		Query query = em.createNamedQuery("DemographicAnalysis.findRecentAnalysesFroAccount");
		query.setParameter("account", account);
		query.setMaxResults(end - begin);
		query.setFirstResult(begin);
		
		return query.getResultList();
	}
	
	@Override
	@Transactional
	public void create(Analysis entity) {
		UUID hash = UUID.randomUUID();
		while (findByHash(hash) != null){
			hash = UUID.randomUUID();
		}
		entity.setHash(hash.toString());
		getEntityManager().persist(entity);
	}

}
