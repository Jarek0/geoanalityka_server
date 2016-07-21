package pl.gisexpert.service;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.ini4j.Wini;
import org.omnifaces.cdi.Eager;
import org.slf4j.Logger;

@Eager
@ApplicationScoped
public class GlobalConfigService {

	public class Clients {
		private String landingPageUrl;
		private String portalUrl;

		public String getLandingPageUrl() {
			return landingPageUrl;
		}

		public String getPortalUrl() {
			return portalUrl;
		}

		public void setLandingPageUrl(String landingPageUrl) {
			this.landingPageUrl = landingPageUrl;
		}

		public void setPortalUrl(String portalUrl) {
			this.portalUrl = portalUrl;
		}
	}

	public class PayU {
		
		private String restUrl;
		private String posId;
		private String clientKey;
		private String clientSecret;

		public String getRestUrl() {
			return restUrl;
		}

		public void setRestUrl(String restUrl) {
			this.restUrl = restUrl;
		}

		public String getPosId() {
			return posId;
		}

		public void setPosId(String posId) {
			this.posId = posId;
		}

		public String getClientKey() {
			return clientKey;
		}

		public void setClientKey(String clientKey) {
			this.clientKey = clientKey;
		}

		public String getClientSecret() {
			return clientSecret;
		}

		public void setClientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
		}
	}
	
	public class Settings {
		private Integer baseCredits;
		private String contactFormTarget;
		
		public Integer getBaseCredits() {
			return baseCredits;
		}

		public void setBaseCredits(Integer baseCredits) {
			this.baseCredits = baseCredits;
		}

		public String getContactFormTarget() {
			return contactFormTarget;
		}

		public void setContactFormTarget(String contactFormTarget) {
			this.contactFormTarget = contactFormTarget;
		}
		
	}

	private Clients clients;
	private PayU payu;
	private Settings settings;

	@Inject
	Logger log;

	@PostConstruct
	public void init() {
		Wini config;
		try {
			config = new Wini(getClass().getResource("/config.ini"));

			clients = new Clients();
			clients.setPortalUrl(config.get("clients", "portal", String.class));
			clients.setLandingPageUrl(config.get("clients", "landingpage", String.class));

			payu = new PayU();
			payu.setRestUrl(config.get("payu", "rest_url", String.class));
			payu.setPosId(config.get("payu", "pos_id", String.class));
			payu.setClientKey(config.get("payu", "client_key", String.class));
			payu.setClientSecret(config.get("payu", "client_secret", String.class));
			
			settings = new Settings();
			settings.setBaseCredits(config.get("settings", "base_credits", Integer.class));
			settings.setContactFormTarget(config.get("settings", "contact_form_target", String.class));

		} catch (IOException ex) {
			log.error(null, ex);
		}
	}

	public Clients getClients() {
		return clients;
	}

	public PayU getPayu() {
		return payu;
	}

	public Settings getSettings() {
		return settings;
	}
	
}
