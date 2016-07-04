package pl.gisexpert.payu.client;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import pl.gisexpert.payu.model.CreateOrderResponse;
import pl.gisexpert.payu.model.OrderBase;
import pl.gisexpert.payu.service.PayUAuthService;
import pl.gisexpert.service.GlobalConfigService;

@Named
public class PayUClient {

	@Inject
	private GlobalConfigService appConfig;

	@Inject
	private PayUAuthService payuAuthService;

	@Inject
	Logger log;

	private Client client;
	private WebTarget createOrderTarget;

	public String createOrder(OrderBase createOrderForm) {

		client = ClientBuilder.newClient();
		createOrderTarget = client.target(appConfig.getPayu().getRestUrl() + "/orders");

		MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<>();
		myHeaders.add("Content-Type", "application/json");
		myHeaders.add("Authorization", "Bearer " + payuAuthService.getBearerToken().toString());

		Response response = createOrderTarget.request().accept(MediaType.APPLICATION_JSON).headers(myHeaders)
				.post(Entity.entity(createOrderForm, MediaType.APPLICATION_JSON));

		CreateOrderResponse responseEntity = response.readEntity(CreateOrderResponse.class); // (CreateOrderResponse)
																								// response.getEntity();
		client.close();

		switch (responseEntity.getStatus().getStatusCode()) {
		case "SUCCESS":
			log.info("New PayU payment. Order id: " + responseEntity.getExtOrderId() + ", PayU order ID: "
					+ responseEntity.getOrderId());

			return responseEntity.getRedirectUri();
		default:
			log.warn("PayU payment failed. Order id: " + responseEntity.getExtOrderId() + ", status: "
					+ responseEntity.getStatus().getCode() + ", code: " + responseEntity.getStatus().getStatusCode());
			return null;
		}

	}
}
