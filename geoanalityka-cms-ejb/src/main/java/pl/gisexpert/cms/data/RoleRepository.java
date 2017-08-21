package pl.gisexpert.cms.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import pl.gisexpert.cms.model.Role;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

import java.util.List;

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

	public Role findByName(String name) throws NoResultException{
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Role> cq = cb.createQuery(Role.class);
		Root<Role> role = cq.from(Role.class);
		cq.select(role);
		cq.where(cb.equal(role.get("name"), name));
		TypedQuery<Role> q = getEntityManager().createQuery(cq);
		try {
		return q.getSingleResult();
		}
		catch (Exception e){
			return null;
		}
	}

    public List<String> findAllAdminsUsernames() throws NoResultException{
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Role> role = cq.from(Role.class);
		cq.distinct(true).select(role.join("accounts").get("username"));
		cq.where(cb.equal(role.get("name"), "Administrator"));
		TypedQuery<String> q = getEntityManager().createQuery(cq);

		try {
		return q.getResultList();
		}
		catch (Exception e){
			return null;
		}
    }
}
