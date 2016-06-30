package pl.gisexpert.cms.service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import pl.gisexpert.cms.model.AccessToken;
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
}
