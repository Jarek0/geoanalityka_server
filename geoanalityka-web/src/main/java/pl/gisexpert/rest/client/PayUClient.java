package pl.gisexpert.rest.client;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.google.gson.Gson;

import pl.gisexpert.model.payu.CreateOrderResponse;
import pl.gisexpert.model.payu.OrderBase;
import pl.gisexpert.service.GlobalConfigService;

@ApplicationScoped
public class PayUClient {

	@Inject
	GlobalConfigService appConfig;

	@Inject
	Logger log;

	private Client client;
	private WebTarget createOrderTarget;

	@PostConstruct
	public void init() {
		client = ClientBuilder.newClient();
		System.out.println(appConfig.getPayu().getRestUrl());
		createOrderTarget = client.target(appConfig.getPayu().getRestUrl() + "/orders");

	}

	public String createOrder(OrderBase createOrderForm) {

		Gson gson = new Gson();
		String createOrderFormJson = gson.toJson(createOrderForm);

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<>();
		myHeaders.add("Content-Type", "accplication/json");
		myHeaders.add("Authorization", "Bearer db9a102f-28d9-4c79-baed-09c2f38ab219");

		Response response = createOrderTarget.request().accept(MediaType.APPLICATION_JSON).headers(myHeaders)
				.post(Entity.entity(createOrderForm, MediaType.APPLICATION_JSON));

		CreateOrderResponse responseEntity = (CreateOrderResponse) response.getEntity();
		System.out.println("Zapytanie do payu:" + createOrderFormJson);
		String responseJson = gson.toJson(responseEntity);

		log.info("New PayU payment. Order id: " + responseEntity.getExtOrderId() + ", PayU order ID: "
				+ responseEntity.getOrderId());
		
		return responseEntity.getRedirectUri();
	}
}
