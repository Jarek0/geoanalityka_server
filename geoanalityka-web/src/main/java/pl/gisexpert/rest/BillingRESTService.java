/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.gisexpert.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.InvoiceRepository;
import pl.gisexpert.cms.data.OrderRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.CompanyAccount;
import pl.gisexpert.cms.model.NaturalPersonAccount;
import pl.gisexpert.cms.model.Order;
import pl.gisexpert.cms.model.OrderStatus;
import pl.gisexpert.cms.model.OrderType;
import pl.gisexpert.cms.model.PremiumPlanType;
import pl.gisexpert.cms.service.BillingService;
import pl.gisexpert.cms.service.PremiumPlanService;
import pl.gisexpert.cms.visitor.DefaultAccountVisitor;
import pl.gisexpert.invoice.InvoiceDocFactory;
import pl.gisexpert.invoice.model.Address;
import pl.gisexpert.invoice.model.Company;
import pl.gisexpert.invoice.model.Invoice;
import pl.gisexpert.invoice.model.Transaction;
import pl.gisexpert.invoice.model.TransactionItem;
import pl.gisexpert.payu.client.PayUClient;
import pl.gisexpert.payu.model.Buyer;
import pl.gisexpert.payu.model.CreateOrderNotify;
import pl.gisexpert.payu.model.OrderBase;
import pl.gisexpert.payu.model.Product;
import pl.gisexpert.rest.model.AddCreditForm;
import pl.gisexpert.rest.model.BaseResponse;
import pl.gisexpert.rest.model.BillingHistoryResponse;
import pl.gisexpert.rest.model.OrderInfo;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.service.GlobalConfigService.PayU;

@Path("/billing")
public class BillingRESTService {

	private final int DEFAULT_HISTORY_LIMIT = 10;

	@Inject
	private AccountRepository accountRepository;

	@Inject
	private OrderRepository orderRepository;

	@Inject
	private InvoiceRepository invoiceRepository;

	@Inject
	private BillingService billingService;

	@Inject
	private PremiumPlanService premiumPlanService;

	@Inject
	private GlobalConfigService appConfig;

	@Inject
	private PayUClient payuClient;

	@Inject
	private Logger log;

	@POST
	@Path("/add_credit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerAccount(@Context HttpServletRequest request, AddCreditForm formData) {

		PayU payUSettings = appConfig.getPayu();

		Account buyerAccount = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

		OrderBase createOrderForm = new OrderBase();
		createOrderForm.setCurrencyCode("PLN");

		String customerIpAddress = request.getHeader("X-Forwarded-For");
		if (customerIpAddress == null || customerIpAddress.length() == 0
				|| "unknown".equalsIgnoreCase(customerIpAddress)) {
			customerIpAddress = request.getHeader("Proxy-Client-IP");
		}
		if (customerIpAddress == null || customerIpAddress.length() == 0
				|| "unknown".equalsIgnoreCase(customerIpAddress)) {
			customerIpAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (customerIpAddress == null || customerIpAddress.length() == 0
				|| "unknown".equalsIgnoreCase(customerIpAddress)) {
			customerIpAddress = request.getHeader("HTTP_CLIENT_IP");
		}
		if (customerIpAddress == null || customerIpAddress.length() == 0
				|| "unknown".equalsIgnoreCase(customerIpAddress)) {
			customerIpAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (customerIpAddress == null || customerIpAddress.length() == 0
				|| "unknown".equalsIgnoreCase(customerIpAddress)) {
			customerIpAddress = request.getRemoteAddr();
		}

		StringTokenizer stk = new StringTokenizer(customerIpAddress, ":");
		if (stk.hasMoreTokens()) {
			customerIpAddress = stk.nextToken();
		}

		OrderType orderType = OrderType.NONE;
		String productName;
		switch (formData.getAmount()) {
		case 50:
			productName = "Geoanalizy - aktywacja / przedłużenie planu standardowego";
			orderType = OrderType.STANDARD_PLAN_ACTIVATION;
			break;
		case 100:
			productName = "Geoanalizy - aktywacja / przedłużenie planu zaawansowanego";
			orderType = OrderType.ADVANCED_PLAN_ACTIVATION;
			break;
		default:
			return Response.status(Response.Status.UNAUTHORIZED).entity(null).build();
		}
		createOrderForm.setDescription("Środki do wykorzystania w portalu Geoanalizy");
		createOrderForm.setCustomerIp(customerIpAddress);
		createOrderForm.setMerchantPosId(payUSettings.getPosId());
		createOrderForm.setNotifyUrl("http://mapy.gis-expert.pl/geoanalityka-web/rest/billing/payu_notify");

		List<Product> products = new ArrayList<>();
		Product product = new Product(productName, formData.getAmount() * 100, 1);
		products.add(product);

		createOrderForm.setProducts(products);
		createOrderForm.setTotalAmount(formData.getAmount() * 100);
		createOrderForm.setBuyer(new Buyer(buyerAccount));

		Order order = new Order();
		order.setStatus(OrderStatus.PENDING);
		order.setBuyer(buyerAccount);
		order.setAmount(formData.getAmount());
		order.setOrderType(orderType);
		order.setDate(new Date());
		
		orderRepository.create(order);
		createInvoice(order);
		
		billingService.addOrder(buyerAccount, order);
		createOrderForm.setExtOrderId(order.getId().toString() + "_" + order.getOrderHash());

		Buyer payuBuyer = new Buyer(buyerAccount);
		createOrderForm.setBuyer(payuBuyer);

		String payuRedirectUrl = payuClient.createOrder(createOrderForm);

		if (payuRedirectUrl == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(null).build();
		}

		BaseResponse responseEntity = new BaseResponse();
		responseEntity.setMessage(payuRedirectUrl);
		responseEntity.setResponseStatus(Response.Status.OK);

		return Response.status(Response.Status.OK).entity(responseEntity).build();
	}

	@POST
	@Path("/payu_notify")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response payuNotify(CreateOrderNotify data) {

		// Extract IP address from an IP:PORT string
		StringTokenizer astk = new StringTokenizer(data.getOrder().getExtOrderId(), "_");
		Long orderId = null;
		if (astk.hasMoreTokens()) {
			orderId = Long.parseLong(astk.nextToken());
		}

		if (orderId == null) {
			log.warn("Incomming order update from PayU. Received invalid order ID: (" + data.getOrder().getExtOrderId()
					+ ")");
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		Order order = orderRepository.find(orderId);
		order.setPayuOrderId(data.getOrder().getOrderId());

		if (order.getStatus() != OrderStatus.COMPLETED) {
			switch (data.getOrder().getStatus()) {
			case "COMPLETED":
				order.setStatus(OrderStatus.COMPLETED);
				Account buyer = order.getBuyer();

				switch (data.getOrder().getProducts().get(0).getUnitPrice()) {
				case 5000:
					buyer.setCredits(buyer.getCredits() + 10);
					premiumPlanService.activatePlan(buyer, PremiumPlanType.PLAN_STANDARDOWY);
					log.debug("Added 10 credits to account: " + buyer.getUsername() + ". PLAN_STANDARDOWY activated.");
					break;
				case 10000:
					buyer.setCredits(buyer.getCredits() + 30);
					premiumPlanService.activatePlan(buyer, PremiumPlanType.PLAN_ZAAWANSOWANY);
					log.debug("Added 30 credits to account: " + buyer.getUsername() + ". PLAN_ZAAWANSOWANY activated.");
					break;
				}

				break;
			case "CANCELED":
				order.setStatus(OrderStatus.CANCELED);
				break;
			case "PENDING":
				order.setStatus(OrderStatus.PENDING);
				break;
			case "REJECTED":
				order.setStatus(OrderStatus.REJECTED);
				break;
			}
			
			orderRepository.edit(order);

		}

		log.info("Incomming order update from PayU. Order " + order.getId() + " has been updated with status: "
				+ order.getStatus().name());

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/unqueue_payment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response unqueuePayment() {

		try {
			Account account = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

			account.setQueuedPayment(null);
			accountRepository.edit(account);

			log.debug("Removed queued payment from account: " + account.getUsername());

		} catch (Exception e) {
			log.debug("Failed to remove queued payment from an account.");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/history/{start}/{limit}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response billingHistory(@PathParam("start") Integer start, @PathParam("limit") Integer limit) {

		Account account = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

		List<Order> orders = billingService.getRecentOrders(account, start, limit);

		BillingHistoryResponse response = new BillingHistoryResponse();
		List<OrderInfo> orderInfos = new ArrayList<>();
		for (Order order : orders) {
			orderInfos.add(new OrderInfo(order));
		}
		response.setOrders(orderInfos);
		response.setResponseStatus(Response.Status.OK);

		return Response.status(Response.Status.OK).entity(response).build();
	}

	@GET
	@Path("/history")
	@Produces(MediaType.APPLICATION_JSON)
	public Response billingHistory() {
		return billingHistory(0, DEFAULT_HISTORY_LIMIT);
	}

	@GET
	@Path("/history/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Response totalBillingItems() {
		Account account = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());
		Integer billingItemsCount = billingService.getBillingItemsCount(account);

		BaseResponse response = new BaseResponse();
		response.setMessage(billingItemsCount.toString());
		response.setResponseStatus(Response.Status.OK);

		return Response.status(Response.Status.OK).entity(response).build();
	}

	@GET
	@Path("/invoice/{orderHash}.rtf")
	@Produces("application/rtf")
	public Response getRtfInvoice(@PathParam("orderHash") String orderHash) {

		Account account = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

		Order order = orderRepository.findByHash(UUID.fromString(orderHash));
		if (order == null || !order.getBuyer().equals(account)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		pl.gisexpert.cms.model.Invoice invoice = billingService.getRtfInvoice(order, false);

		return Response.status(Response.Status.OK).entity(invoice.getInvoiceData()).build();

	}

	public pl.gisexpert.cms.model.Invoice createInvoice(Order order) {

		Account account = accountRepository.findByUsername(order.getBuyer().getUsername());
		account = accountRepository.fetchContactData(account);
		
		Invoice invoice = new Invoice(billingService.nextInvoiceId());
		final Address invoiceAddress = new Address();
		
		final Company invoiceCompany = new Company();
		invoiceCompany.setAddress(invoiceAddress);
		
		account.accept(new DefaultAccountVisitor() {
			@Override
			public void visit(CompanyAccount account) {
				
				pl.gisexpert.cms.model.Company company = account.getCompany();
				pl.gisexpert.cms.model.Address address = company.getAddress();
				
				invoiceAddress.setCity(address.getCity());
				invoiceAddress.setFlatNumber(address.getFlatNumber());
				invoiceAddress.setHouseNumber(address.getHouseNumber());
				invoiceAddress.setStreet(address.getStreet());
				invoiceAddress.setZipcode(address.getZipcode());
				
				invoiceCompany.setCompanyName(company.getCompanyName());
				invoiceCompany.setPhone(company.getPhone());
				invoiceCompany.setTaxId(company.getTaxId());
				
			}
			@Override
			public void visit(NaturalPersonAccount account) {

				pl.gisexpert.cms.model.Address address = account.getAddress();
				
				invoiceAddress.setCity(address.getCity());
				invoiceAddress.setFlatNumber(address.getFlatNumber());
				invoiceAddress.setHouseNumber(address.getHouseNumber());
				invoiceAddress.setStreet(address.getStreet());
				invoiceAddress.setZipcode(address.getZipcode());
				
				invoiceCompany.setPhone(account.getPhone());
				invoiceCompany.setCompanyName(account.getFirstName() + " " + account.getLastName());
			}
		});
		
		invoice.setCompany(invoiceCompany);

		Transaction transaction = new Transaction();

		String itemName = "";
		switch (order.getOrderType()) {
		case NONE:
			itemName = "Doładowanie środków";
			break;
		case STANDARD_PLAN_ACTIVATION:
			itemName = "Aktywacja planu standardowego";
			break;
		case ADVANCED_PLAN_ACTIVATION:
			itemName = "Aktywacja planu zaawansowanego";
			break;
		case DEDICATED_PLAN_ACTIVATION:
			itemName = "Aktywacja planu dedykowanego";
			break;
		}

		TransactionItem item = new TransactionItem(1, itemName, order.getAmount().doubleValue(), 1.0, 0.23);

		List<TransactionItem> items = Arrays.asList(item);
		transaction.setItems(items);
		invoice.setTransaction(transaction);

		InvoiceDocFactory docFactory = new InvoiceDocFactory();
		byte[] invoiceOriginalRtfDocument = docFactory.createRtf(invoice);
		
		invoice.setOriginal(false);
		byte[] invoiceCopyRtfDocument = docFactory.createRtf(invoice);
		
		// Original invoice document
		pl.gisexpert.cms.model.Invoice invoiceEntity = new pl.gisexpert.cms.model.Invoice();
		invoiceEntity.setInvoiceData(invoiceOriginalRtfDocument);
		invoiceEntity.setMimeType("application/rtf");
		invoiceEntity.setSerialId(invoice.getInvoiceSerialId());
		invoiceEntity.setDateCreated(new Date());
		invoiceEntity.setOriginal(true);
		invoiceEntity.setOrder(order);
		invoiceRepository.create(invoiceEntity);
		
		// Invoice copy
		invoiceEntity = new pl.gisexpert.cms.model.Invoice();
		invoiceEntity.setInvoiceData(invoiceCopyRtfDocument);
		invoiceEntity.setMimeType("application/rtf");
		invoiceEntity.setSerialId(invoice.getInvoiceSerialId());
		invoiceEntity.setDateCreated(new Date());
		invoiceEntity.setOriginal(false);
		invoiceEntity.setOrder(order);
		invoiceRepository.create(invoiceEntity);
		
		return invoiceEntity;

	}
}
