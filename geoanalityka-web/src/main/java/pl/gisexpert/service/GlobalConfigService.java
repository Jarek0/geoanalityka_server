package pl.gisexpert.service;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.ini4j.Wini;
import org.slf4j.Logger;

@ApplicationScoped
public class GlobalConfigService {

	private String landingPageUrl;
	private String portalUrl;

	@Inject
	Logger log;

	@PostConstruct
	public void init() {
		Wini config;
		try {
			config = new Wini(getClass().getResource("/config.ini"));
			portalUrl = config.get("clients", "portal", String.class);
			landingPageUrl = config.get("clients", "landingpage", String.class);

		} catch (IOException ex) {
			log.error(null, ex);
		}
	}

	public String getLandingPageUrl() {
		return landingPageUrl;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

}
