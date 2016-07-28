package pl.gisexpert.cms.data;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class AccountRepository extends AbstractRepository<Account> {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AccountRepository() {
		super(Account.class);
	}

	@Transactional
	public Account findByUsername(String username, Boolean fetchCompany, Boolean fetchTokens) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		if (fetchCompany) {
			account.fetch("company", JoinType.INNER);
		}
		cq.select(account);
		cq.where(cb.equal(account.get("username"), username));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			Account resultAccount = q.getSingleResult();
			if (fetchTokens) {
				Hibernate.initialize(resultAccount.getTokens());
			}
			return resultAccount;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Account findByUsername(String username, Boolean fetchAddress) {
		return findByUsername(username, fetchAddress, false);
	}

	public Account findByUsername(String username) {
		return findByUsername(username, false, false);
	}

	public Account findByEmail(String email) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("emailAddress"), email));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			Account resultAccount = q.getSingleResult();
			return resultAccount;
		} catch (Exception e) {
			return null;
		}
	}

	public void removeRoles(Account account, List<Role> roles) {

		for (Role role : roles) {
			Query query = getEntityManager().createNamedQuery("Account.removeRole");
			query.setParameter(1, account.getUsername());
			query.setParameter(2, role.getName());
			query.executeUpdate();
		}
	}

	public Account findByResetPasswordToken(String token) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("resetPassword").get("token"), token));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			Account resultAccount = q.getSingleResult();
			return resultAccount;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	@Transactional
	public void create(Account account){
		if (account.getRoles() == null) {
			List<Role> roles = new ArrayList<>();
			Role role = new Role();
			role.setName("PLAN_TESTOWY");
			account.setRoles(roles);
		}
		em.persist(account);
	}

}
