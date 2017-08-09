package pl.gisexpert.cms.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;

import pl.gisexpert.cms.model.*;
import pl.gisexpert.cms.qualifier.CMSEntityManager;
import pl.gisexpert.cms.visitor.DefaultAccountVisitor;

@ApplicationScoped
public class AccountRepository extends AbstractRepository<Account> {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Inject
	private Logger log;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AccountRepository() {
		super(Account.class);
	}

	@Transactional
	public Account findByUsername(String username) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("username"), username));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			Account resultAccount = q.getSingleResult();
			return resultAccount;
		} catch (NoResultException e) {
			log.debug("Account with username: " + username + " could not be found.");
			return null;
		} catch (Exception e) {
			log.debug("An exception occurred while retrieving account with username: " + username + " from db.");
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public Address findAddressByUsername(String username){
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Address> cq = cb.createQuery(Address.class);
		Root<NaturalPersonAccount> account = cq.from(NaturalPersonAccount.class);
		cq.select(account.join("address"));
		cq.where(cb.equal(account.get("username"), username));
		TypedQuery<Address> q = getEntityManager().createQuery(cq);
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			log.debug("Address of username: " + username + " could not be found.");
			return null;
		} catch (Exception e) {
			log.debug("An exception occurred while retrieving address of username: " + username + " from db.");
			e.printStackTrace();
			return null;
		}
	}


	@Transactional
	public Account fetchContactData(Account account) {
		Account resultAccount = em.getReference(Account.class, account.getId());
		resultAccount.accept(new DefaultAccountVisitor() {
			@Override
			public void visit(NaturalPersonAccount account) {
				if (!Hibernate.isInitialized(account.getAddress())) {
					Hibernate.initialize(account.getAddress());
				}
			}
		});

		return resultAccount;

	}

	public Account findByEmail(String email) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("username"), email));
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
	public void create(Account account) {
		em.persist(account);
	}

    public Account findByToken(String confirmationToken) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("accountConfirmation").get("token"), confirmationToken));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			Account resultAccount = q.getSingleResult();
			return resultAccount;
		} catch (Exception e) {
			return null;
		}
    }
}
