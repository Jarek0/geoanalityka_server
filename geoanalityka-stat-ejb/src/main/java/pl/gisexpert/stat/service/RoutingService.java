package pl.gisexpert.stat.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;

import pl.gisexpert.model.gis.Coordinate;
import pl.gisexpert.stat.qualifier.StatEntityManager;

@ApplicationScoped
public class RoutingService {

	@Inject
	@StatEntityManager
	private EntityManager em;

	@Inject
	private Logger log;

	protected EntityManager getEntityManager() {
		return em;
	}

	public String createGeoJSONServiceArea(Coordinate initialPoint, Integer travelTime, Character travelType) {

		log.debug("Creating service area.");
		log.debug("Travel type: " + travelType);
		log.debug("Travel time: " + travelTime);
		
		log.debug("Strating coordinates = X:" + initialPoint.getX() + ", Y:" + initialPoint.getY() + ", EPSG:"
				+ initialPoint.getEpsgCode());
		String queryString = "SELECT service_area(" + travelTime + "," + initialPoint.getX() + "," + initialPoint.getY()
				+ ",4326, '" + travelType + "')";
		Query query = em.createNativeQuery(queryString);

		String result = (String) query.getSingleResult();

		return result;
	}

}
