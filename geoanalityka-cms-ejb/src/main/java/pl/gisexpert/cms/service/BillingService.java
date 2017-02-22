package pl.gisexpert.cms.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Invoice;
import pl.gisexpert.cms.model.Order;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@Stateless
public class BillingService {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	public Account addOrder(Account account, Order order) {
		account = em.find(Account.class, account.getId());
		account.getOrders().add(order);
		account.setLastLoginDate(new Date());
		em.merge(account);
		return account;
	}

	public void addInvoice(Order order, Invoice invoice) {
		order.getInvoices().add(invoice);
		em.merge(order);
	}

	public String nextInvoiceId() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		String prevOrderQueryString = "SELECT count(id) FROM invoices WHERE EXTRACT(year FROM date_created) = "
				+ currentYear;
		Integer invoicesInCurrentYear;
		try {
			invoicesInCurrentYear = ((Number) em.createNativeQuery(prevOrderQueryString).getSingleResult()).intValue();
		} catch (NoResultException e) {
			invoicesInCurrentYear = 0;
		}

		return "GA/" + currentYear + "/" + (invoicesInCurrentYear + 1);
	}

	public Invoice getRtfInvoice(Order order, Boolean original) {
		String queryString = "Invoice.getInvoiceByOrderAndType";
		TypedQuery<Invoice> query = em.createNamedQuery(queryString, Invoice.class);
		query.setParameter("order", order);
		query.setParameter("original", original);
		query.setParameter("mimeType", "application/rtf");
		query.setMaxResults(1);

		Invoice invoice = query.getResultList().get(0);
		return invoice;
	}
	
	public Invoice getPdfInvoice(Order order, Boolean original) {
		String queryString = "Invoice.getInvoiceByOrderAndType";
		TypedQuery<Invoice> query = em.createNamedQuery(queryString, Invoice.class);
		query.setParameter("order", order);
		query.setParameter("original", original);
		query.setParameter("mimeType", "application/pdf");
		query.setMaxResults(1);

		Invoice invoice = query.getResultList().get(0);
		return invoice;
	}

	public List<Order> getRecentOrders(Account account, Integer start, Integer limit) {
		String queryString = "SELECT order FROM Order order WHERE order.buyer = :buyer ORDER BY order.date DESC";
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
