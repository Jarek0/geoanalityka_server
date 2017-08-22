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

import org.slf4j.Logger;

import pl.gisexpert.cms.model.*;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

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

	public boolean checkIfUserWithThisMailExist(String email){
		Account searched = findByEmail(email);
		return searched != null;
	}

	public Address findAddressByUsername(String username) throws NoResultException{
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Address> cq = cb.createQuery(Address.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account.join("address"));
		cq.where(cb.equal(account.get("username"), username));
		TypedQuery<Address> q = getEntityManager().createQuery(cq);

		try {
			return q.getSingleResult();
		}
		catch (Exception e){
			return null;
		}
	}

	public Account findById(Long id) throws NoResultException{
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("id"), id));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
		return q.getSingleResult();
		}
		catch (Exception e){
			return null;
		}
	}

	@Transactional
	public Account fetchContactData(Account account) {

		return em.getReference(Account.class, account.getId());

	}

	public Account findByEmail(String email)  throws NoResultException{
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("username"), email));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);

		try {
			return q.getSingleResult();
		}
		catch (Exception e){
			return null;
		}
	}

	@Transactional
	public void removeRoles(Account account, List<Role> roles) {

		for (Role role : roles) {
			Query query = getEntityManager().createNamedQuery("Account.removeRole");
			query.setParameter(1, account.getUsername());
			query.setParameter(2, role.getName());
			query.executeUpdate();
		}
	}

	public Account findByResetPasswordToken(String token) throws NoResultException{

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("resetPassword").get("token"), token));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);
		try {
		return q.getSingleResult();
		}
		catch (Exception e){
			return null;
		}
	}

	@Override
	@Transactional
	public void create(Account account) {
		em.persist(account);
	}

    public Account findByToken(String confirmationToken) throws NoResultException{
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> account = cq.from(Account.class);
		cq.select(account);
		cq.where(cb.equal(account.get("accountConfirmation").get("token"), confirmationToken));
		TypedQuery<Account> q = getEntityManager().createQuery(cq);
		try {
		return q.getSingleResult();
		}
		catch (Exception e){
			return null;
		}
    }
}
