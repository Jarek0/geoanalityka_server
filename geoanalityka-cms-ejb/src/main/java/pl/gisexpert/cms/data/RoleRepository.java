package pl.gisexpert.cms.data;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class RoleRepository extends AbstractRepository<Role> {

    @Inject
    @CMSEntityManager
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public RoleRepository() {
        super(Role.class);
    }
    
    public Role findByName(String name) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> role = cq.from(Role.class);
        cq.select(role);
        cq.where(cb.equal(role.get("name"), name));
        TypedQuery<Role> q = getEntityManager().createQuery(cq);
        
        try {
            Role result = q.getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    public void removeRoleFromAllAccounts(Role role) {
        Query query = getEntityManager().createNamedQuery("Role.removeRoleFromAllAccounts");
        query.setParameter(1, role.getName());
        query.executeUpdate();
        
        getEntityManager().getEntityManagerFactory().getCache().evict(Account.class);
    }
    
}
