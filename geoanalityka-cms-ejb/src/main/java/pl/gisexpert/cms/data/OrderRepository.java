package pl.gisexpert.cms.data;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import pl.gisexpert.cms.model.Order;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class OrderRepository extends AbstractRepository<Order> {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public OrderRepository() {
		super(Order.class);
	}
	
	@Transactional
	public Order findByHash(UUID hash) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> order = cq.from(Order.class);
		cq.select(order);
		cq.where(cb.equal(order.get("orderHash"), hash.toString()));
		TypedQuery<Order> q = getEntityManager().createQuery(cq);

		try {
			Order resultOrder = q.getSingleResult();
			return resultOrder;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	@Transactional
	public void create(Order entity) {
		UUID hash = UUID.randomUUID();
		while (findByHash(hash) != null){
			hash = UUID.randomUUID();
		}
		entity.setOrderHash(hash.toString());
		getEntityManager().persist(entity);
	}

}
