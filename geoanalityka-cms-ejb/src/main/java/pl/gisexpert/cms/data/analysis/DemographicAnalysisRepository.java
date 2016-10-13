package pl.gisexpert.cms.data.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.ejb.Startup;
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
import pl.gisexpert.cms.model.analysis.AnalysisStatus;
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

        Query query = em.createNamedQuery("DemographicAnalysis.findRecentAnalysesForAccount");
        query.setParameter("account", account);
        query.setMaxResults(end - begin);
        query.setFirstResult(begin);

        return query.getResultList();
    }

    @Transactional
    public List<DemographicAnalysis> getAnalysesDetailsToCompareForAccount(Account account, List<String> hashes) {

        Query query = em.createNamedQuery("DemographicAnalysis.getAnalysesDetailsToCompareForAccount");
        query.setParameter("account", account);
        query.setParameter("hashes", hashes);


        return query.getResultList();
    }

    @Transactional
    public List<DemographicAnalysis> findMostRecentRangeAndTypeForAccount(Account account, int begin, int end, String orderBy, List<Class> analysisTypes) {
        Query query;

        switch (orderBy) {
            case "dd":
                query = em.createNamedQuery("DemographicAnalysis.findRecentAnalysesByTypeOrderedByDateDESCForAccount");
                break;
            case "da":
                query = em.createNamedQuery("DemographicAnalysis.findRecentAnalysesByTypeOrderedByDateASCForAccount");
                break;
            case "nd":
                query = em.createNamedQuery("DemographicAnalysis.findRecentAnalysesByTypeOrderedByNameDESCForAccount");
                break;
            case "na":
                query = em.createNamedQuery("DemographicAnalysis.findRecentAnalysesByTypeOrderedByNameASCForAccount");
                break;
            default:
                query = em.createNamedQuery("DemographicAnalysis.findRecentAnalysesForAccount");
                break;
        }
        query.setParameter("account", account);
        query.setParameter("classes", analysisTypes);
        System.out.println("Size: " + analysisTypes.size());
        query.setMaxResults(end - begin);
        query.setFirstResult(begin);
        return query.getResultList();
    }

    @Transactional
    public void setDeletedStatusForRecentsByHashesForAccount(Account account, List<String> analysisHashes) {

        Query query = em.createNamedQuery("DemographicAnalysis.setDeletedStatusForAllAnalysesForAccount");
        query.setParameter("account", account);
        query.setParameter("hashes", analysisHashes);
        System.out.println("Size: " + analysisHashes.size());

        query.executeUpdate();
    }

    @Transactional
    public void updateAnalysisNameForAccount(Account account, String name, String hash) {

        Query query = em.createNamedQuery("DemographicAnalysis.updateAnalysisNameForAccount");
        query.setParameter("account", account);
        query.setParameter("name", name);
        query.setParameter("hash", hash);


        query.executeUpdate();
    }

    @Override
    @Transactional
    public void create(Analysis entity) {
        UUID hash = UUID.randomUUID();
        while (findByHash(hash) != null) {
            hash = UUID.randomUUID();
        }
        if (entity.getName() == null || entity.getName().length() == 0) {
            entity.setName("Bez nazwy");
        }
        entity.setHash(hash.toString());
        getEntityManager().persist(entity);
    }

    @Override
    @Transactional
    public void edit(Analysis entity) {
        if (entity.getName() == null || entity.getName().length() == 0) {
            entity.setName("Bez nazwy");
        }
        getEntityManager().merge(entity);
    }

}
