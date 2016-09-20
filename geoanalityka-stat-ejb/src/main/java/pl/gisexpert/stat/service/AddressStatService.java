package pl.gisexpert.stat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.base.Joiner;

import pl.gisexpert.model.gis.Coordinate;
import pl.gisexpert.stat.data.AbstractRepository;
import pl.gisexpert.stat.model.AddressStat;
import pl.gisexpert.stat.qualifier.StatEntityManager;

@ApplicationScoped
public class AddressStatService {

	@Inject
	@StatEntityManager
	private EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * 
	 * @param radius
	 * @param point
	 * @return
	 */
	public Integer sumAllInRadius(Integer radius, Coordinate point) {
		Query query = em.createNamedQuery("AddressStat.SumAllInRadius");
		query.setParameter("x", point.getX());
		query.setParameter("y", point.getY());
		query.setParameter("epsg", point.getEpsgCode());
		query.setParameter("radius", radius);

		Integer result = (Integer) query.getSingleResult();
		result = result == null ? 0 : result;

		return result;
	}
	
	public Integer sumAllPremisesInRadius(Integer radius, Coordinate point) {
		Query query = em.createNamedQuery("AddressStat.SumAllPremisesInRadius");
		query.setParameter("x", point.getX());
		query.setParameter("y", point.getY());
		query.setParameter("epsg", point.getEpsgCode());
		query.setParameter("radius", radius);
		
		Integer result = (Integer) query.getSingleResult();
		result = result == null ? 0 : result;

		return result;
	}

	/**
	 * 
	 * @param range
	 * @param radius
	 * @param point
	 * @return A Map with two keys: "kobiety" and "mezczyzni". Each o them
	 *         contains a Map with keys: 0, 5, 10, 15 ... which contain number
	 *         of people in specific age range. Eg. the key "0" contains number
	 *         of people aged 0 - 4.
	 */
	public HashMap<String, HashMap<Integer, Integer>> sumRangeInRadius(Integer[] range, Integer radius,
			Coordinate point) {

		HashMap<Integer, String> kobietyRangeColumnsMap = new HashMap<>();
		for (int i = 0; i <= 70; i += 5) {
			kobietyRangeColumnsMap.put(i, "przedzialwiekuod" + i + "do" + (i + 4) + "k");
		}
		kobietyRangeColumnsMap.put(75, "przedzialwiekuod75k");

		Map<Integer, String> mezczyzniRangeColumnsMap = new HashMap<>();
		for (int i = 0; i <= 70; i += 5) {
			mezczyzniRangeColumnsMap.put(i, "przedzialwiekuod" + i + "do" + (i + 4) + "m");
		}
		mezczyzniRangeColumnsMap.put(75, "przedzialwiekuod75m");

		List<String> columnSumsArray = new ArrayList<>();
		for (int i = range[0]; i < range[1] && i < 75; i += 5) {
			columnSumsArray.add("sum(" + kobietyRangeColumnsMap.get(i) + ")\\:\\:int as sum_k_" + i + "_" + (i + 4));
			columnSumsArray.add("sum(" + mezczyzniRangeColumnsMap.get(i) + ")\\:\\:int as sum_m_" + i + "_" + (i + 4));
		}
		if (range[1] >= 75) {
			columnSumsArray.add("sum(" + kobietyRangeColumnsMap.get(75) + ")\\:\\:int as sum_k_75");
			columnSumsArray.add("sum(" + mezczyzniRangeColumnsMap.get(75) + ")\\:\\:int as sum_m_75");
		}
		String columnSumsStr = Joiner.on(",").join(columnSumsArray);

		String queryString = "SELECT " + columnSumsStr
				+ " FROM stat2015_stan_na_30_05_2016 WHERE ST_Within(geom,ST_Buffer(ST_Transform(ST_GeomFromText('POINT(' || :x || ' ' || :y || ')', :epsg), 2180), :radius))";

		Query query = em.createNativeQuery(queryString);

		query.setParameter("x", point.getX());
		query.setParameter("y", point.getY());
		query.setParameter("epsg", point.getEpsgCode());
		query.setParameter("radius", radius);

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		Object[] result = results.get(0);

		if (result == null) {
			return null;
		}

		HashMap<Integer, Integer> kobiety = new HashMap<>();
		HashMap<Integer, Integer> mezczyzni = new HashMap<>();

		for (int i = 0; i < result.length; i += 2) {
			kobiety.put(range[0] + ((i / 2) * 5), (Integer) result[i]);
			mezczyzni.put(range[0] + ((i / 2) * 5), (Integer) result[i + 1]);
		}

		HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniValues = new HashMap<>();
		kobietyAndMezczyzniValues.put("kobiety", kobiety);
		kobietyAndMezczyzniValues.put("mezczyzni", mezczyzni);

		return kobietyAndMezczyzniValues;

	}

}
