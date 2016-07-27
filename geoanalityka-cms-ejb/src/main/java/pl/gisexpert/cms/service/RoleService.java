package pl.gisexpert.cms.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@Stateless
public class RoleService {

	@Inject
    @CMSEntityManager
    private EntityManager em;
	
	public void removeRoleFromAllAccounts(Role role) {
		Query query = em.createNamedQuery("Role.removeRoleFromAllAccounts");
		query.setParameter(1, role.getName());
		query.executeUpdate();

		em.getEntityManagerFactory().getCache().evict(Account.class);
	}
	
}
