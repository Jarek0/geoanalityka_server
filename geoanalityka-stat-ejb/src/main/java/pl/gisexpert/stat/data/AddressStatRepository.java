package pl.gisexpert.stat.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pl.gisexpert.stat.model.AddressStat;
import pl.gisexpert.stat.qualifier.StatEntityManager;

@ApplicationScoped
public class AddressStatRepository extends AbstractRepository<AddressStat> {

    @Inject
    @StatEntityManager
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AddressStatRepository() {
        super(AddressStat.class);
    }

    /*public AddressStat findByUsername(String username) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AddressStat> cq = cb.createQuery(AddressStat.class);
        Root<AddressStat> addressStat = cq.from(AddressStat.class);
        cq.select(addressStat);
        cq.where(cb.equal(addressStat.get("username"), username));
        TypedQuery<AddressStat> q = getEntityManager().createQuery(cq);

        try {
            AddressStat resultAddressStat = q.getSingleResult();
            return resultAddressStat;
        } catch (Exception e) {
            return null;
        }
    }*/

}
