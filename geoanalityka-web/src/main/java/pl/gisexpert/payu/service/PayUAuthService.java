package pl.gisexpert.payu.service;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import pl.gisexpert.payu.model.AuthResponse;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.service.GlobalConfigService.PayU;

@Startup
@Singleton
@ApplicationScoped
public class PayUAuthService {

	private static UUID accessToken;

	@Inject
	private Logger log;

	@Inject
	private GlobalConfigService appConfig;

	@PostConstruct
	@Schedule(hour = "*/1", minute = "0", second = "0", persistent = true)
	private void refreshToken() {

		Client client;
		WebTarget authTarget;

		client = ClientBuilder.newClient();
		authTarget = client.target("https://secure.payu.com/pl/standard/user/oauth/authorize");

		PayU payuConfig = appConfig.getPayu();

		MultivaluedMap<String, String> authFormValues = new MultivaluedHashMap<>();
		authFormValues.add("grant_type", "client_credentials");
		authFormValues.add("client_id", payuConfig.getPosId());
		authFormValues.add("client_secret", payuConfig.getClientSecret());

		Form authDataForm = new Form(authFormValues);

		Response response = authTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.form(authDataForm));

		AuthResponse responseEntity = response.readEntity(AuthResponse.class); // (CreateOrderResponse)
																				// response.getEntity();

		if (responseEntity.getError() != null) {
			log.warn("Failed to generate PayU access token. PayU error: " + responseEntity.getError() + ", "
					+ responseEntity.getErrorDescription());
		} else {
			log.info("Successfully generated PayU access token");
			accessToken = UUID.fromString(responseEntity.getAccessToken());
		}

		client.close();
	}

	public UUID getBearerToken() {
		return accessToken;
	}
}
