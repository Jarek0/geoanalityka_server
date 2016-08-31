package pl.gisexpert.cms.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Order;
import pl.gisexpert.cms.qualifier.CMSEntityManager;


@Stateless
public class BillingService {

    @Inject
    @CMSEntityManager
    private EntityManager em;

    public Account addOrder(Account account, Order order){
    	account = em.find(Account.class, account.getId());
    	account.getOrders().add(order);
    	account.setLastLoginDate(new Date());
    	em.merge(account);
    	return account;
    }
    
    public List<Order> getRecentOrders(Account account, Integer start, Integer limit) {
    	String queryString = "SELECT order FROM Order order WHERE order.buyer = :buyer";
    	TypedQuery<Order> query = em.createQuery(queryString, Order.class);
    	
    	
    	query.setParameter("buyer", account);
    	query.setFirstResult(start);
    	query.setMaxResults(limit);
 
    	List<Order> orders = query.getResultList();
   
    	return orders;
    }
    
    public Integer getBillingItemsCount(Account account) {
    	String queryString = "SELECT COUNT(order) FROM Order order WHERE order.buyer = :buyer";
    	TypedQuery<Integer> query = em.createQuery(queryString, Integer.class);
    	
    	return query.getSingleResult();
    }
}
