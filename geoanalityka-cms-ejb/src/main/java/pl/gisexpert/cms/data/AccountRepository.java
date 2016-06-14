package pl.gisexpert.cms.data;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

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

	public Account findByUsername(String username, Boolean fetchAddress) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		if (fetchAddress) {
			account.fetch("address", JoinType.INNER);
		}
		cq.select(account);
		cq.where(cb.equal(account.get("username"), username));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			Account resultAccount = q.getSingleResult();
			return resultAccount;
		} catch (Exception e) {
			return null;
		}
	}

	public Account findByUsername(String username) {
		return findByUsername(username, false);
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

	public void removeRoles(Account account, Collection<Role> roles) {

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
		cq.where(cb.equal(account.get("resetPassword").get("reset_pass_token"), token));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			Account resultAccount = q.getSingleResult();
			return resultAccount;
		} catch (Exception e) {
			return null;
		}
	}

}
