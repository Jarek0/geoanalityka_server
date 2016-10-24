package pl.gisexpert.stat.service;

import java.util.Date;
import java.util.HashMap;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.analysis.DemographicAnalysisRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.analysis.AnalysisStatus;
import pl.gisexpert.cms.model.analysis.AnalysisStatusCode;
import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;
import pl.gisexpert.cms.model.analysis.demographic.PeopleByWorkingAgeSums;
import pl.gisexpert.cms.model.analysis.demographic.SimpleDemographicAnalysis;
import pl.gisexpert.cms.service.AnalysisCostCalculator;
import pl.gisexpert.cms.service.AnalysisService;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AsyncAnalysisService {

	private final static int MIN_VALID_POPULATION = 50;
	
	@Inject
	private AddressStatService addressStatService;
	
	@Inject
	private RoutingService routingService;
	
	@Inject
	private AnalysisService analysisService;
	
	@Inject
	private AccountRepository accountRepository;
	
	@Inject
	private DemographicAnalysisRepository demographicAnalysisRepository;
	
	@Inject
	private AnalysisCostCalculator analysisCostCalculator;

	@Asynchronous
	public void executeSimpleDemographicAnalysis(SimpleDemographicAnalysis analysis) {
		
		demographicAnalysisRepository.create(analysis);
		
		Account creator = analysis.getCreator();
		AnalysisStatus status = AnalysisStatus.PENDING;
		AnalysisStatusCode statusCode = null;

		Integer totalPopulation = 0;
		Integer inhabitedPremises = 0;
		
		switch (analysis.getAreaType()) {
		case RADIUS:
			totalPopulation = addressStatService.sumAllInRadius(analysis.getRadius(), analysis.getLocation());
			inhabitedPremises = addressStatService.sumAllPremisesInRadius(analysis.getRadius(),
					analysis.getLocation());
			status = AnalysisStatus.FINISHED;
			statusCode = AnalysisStatusCode.OK;
			break;
		case TRAVEL_TIME:
			String geojsonArea = routingService.createGeoJSONServiceArea(analysis.getLocation(), analysis.getTravelTimeOrDistance(),
					analysis.getTravelType().name().toCharArray()[0]);

			if (geojsonArea == null) {
				statusCode = AnalysisStatusCode.SERVICE_AREA_FAIL;
				status = AnalysisStatus.FAILED;
			} else {
				
				analysis.setGeojsonArea(geojsonArea);
				totalPopulation = addressStatService.sumAllInPolygon(geojsonArea);
				inhabitedPremises = addressStatService.sumAllPremisesInPolygon(geojsonArea);
				
				statusCode = AnalysisStatusCode.OK;
				status = AnalysisStatus.FINISHED;
			}
			
			break;
		}
			
		if (status == AnalysisStatus.FINISHED && !isPopulationHighEnough(totalPopulation)) {
			status = AnalysisStatus.FAILED;
			statusCode = AnalysisStatusCode.POPULATION_TOO_LOW;
		}
		
		if (status != AnalysisStatus.FINISHED) {
			creator.setCredits(creator.getCredits() + analysisCostCalculator.calculate(analysis));
		}
		
		analysis.setPopulation(totalPopulation);
		analysis.setInhabitedPremises(inhabitedPremises);	
		analysis.setDateFinished(new Date());
		analysis.setStatus(status);
		analysis.setStatusCode(statusCode);
		
		analysis = (SimpleDemographicAnalysis) analysisService.addDemographicAnalysis(analysis.getCreator(), analysis);
		demographicAnalysisRepository.edit(analysis);
		accountRepository.edit(creator);
	}
	
	@Asynchronous
	public void executeAdvancedDemographicAnalysis(AdvancedDemographicAnalysis analysis) {
		
		demographicAnalysisRepository.create(analysis);
		
		Account creator = analysis.getCreator();
		AnalysisStatus status = null;
		AnalysisStatusCode statusCode = null;
		HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges;
		Integer inhabitedPremises = 0;
		Integer[] ageRange = Iterables.toArray(Ints.stringConverter().convertAll(Splitter.on("-").split(analysis.getAgeRange())), Integer.class);
		PeopleByWorkingAgeSums peopleByWorkingAgeSums;
		
		switch (analysis.getAreaType()) {
		case RADIUS:
			
			kobietyAndMezczyzniByAgeRanges = addressStatService.sumRangeInRadius(ageRange, analysis.getRadius(), analysis.getLocation());
			inhabitedPremises = addressStatService.sumAllPremisesInRadius(analysis.getRadius(), analysis.getLocation());
			peopleByWorkingAgeSums = addressStatService.peopleByWorkingAgeSums(analysis.getRadius(), analysis.getLocation());

			analysis.setPoprod(peopleByWorkingAgeSums.getPoprod());
			analysis.setProd(peopleByWorkingAgeSums.getProd());
			analysis.setPrzedprod(peopleByWorkingAgeSums.getPrzedprod());

			status = AnalysisStatus.FINISHED;
			statusCode = AnalysisStatusCode.OK;
			break;
		case TRAVEL_TIME:
		default:
			String geojsonArea = routingService.createGeoJSONServiceArea(analysis.getLocation(), analysis.getTravelTimeOrDistance(),
					analysis.getTravelType().name().toCharArray()[0]);

			if (geojsonArea == null) {
				statusCode = AnalysisStatusCode.SERVICE_AREA_FAIL;
				status = AnalysisStatus.FAILED;
				kobietyAndMezczyzniByAgeRanges = null;
			}
			
			else {
				analysis.setGeojsonArea(geojsonArea);
				kobietyAndMezczyzniByAgeRanges = addressStatService.sumRangeInPolygon(ageRange, geojsonArea);
				inhabitedPremises = addressStatService.sumAllPremisesInPolygon(geojsonArea);
				peopleByWorkingAgeSums = addressStatService.peopleByWorkingAgeSumsPolygon(geojsonArea);
				
				analysis.setPoprod(peopleByWorkingAgeSums.getPoprod());
				analysis.setProd(peopleByWorkingAgeSums.getProd());
				analysis.setPrzedprod(peopleByWorkingAgeSums.getPrzedprod());
				
				statusCode = AnalysisStatusCode.OK;
				status = AnalysisStatus.FINISHED;
			}
		}

		if (status == AnalysisStatus.FINISHED && !isPopulationHighEnough(kobietyAndMezczyzniByAgeRanges)) {
			status = AnalysisStatus.FAILED;
			statusCode = AnalysisStatusCode.POPULATION_TOO_LOW;
		}
		
		if (status != AnalysisStatus.FINISHED) {
			creator.setCredits(creator.getCredits() + analysisCostCalculator.calculate(analysis));
		}
		
		analysis.setKobietyAndMezczyzniByAgeRanges(kobietyAndMezczyzniByAgeRanges);
		analysis.setInhabitedPremises(inhabitedPremises);
		analysis.setDateFinished(new Date());
		analysis.setStatus(status);
		analysis.setStatusCode(statusCode);

		analysis = (AdvancedDemographicAnalysis) analysisService.addDemographicAnalysis(analysis.getCreator(), analysis);

		demographicAnalysisRepository.edit(analysis);
		accountRepository.edit(creator);
	}
	

	private boolean isPopulationHighEnough(int population) {
		return population > MIN_VALID_POPULATION;
	}

	private boolean isPopulationHighEnough(HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges) {
		
		if (kobietyAndMezczyzniByAgeRanges == null) {
			return false;
		}
		
		int sum = 0;
		for (Integer value : kobietyAndMezczyzniByAgeRanges.get("kobiety").values()) {
			if (value != null) {
				sum += value;
			}
		}
		for (Integer value : kobietyAndMezczyzniByAgeRanges.get("mezczyzni").values()) {
			if (value != null) {
				sum += value;
			}
		}
		return sum > MIN_VALID_POPULATION;
	}


	public static int getMinValidPopulation() {
		return MIN_VALID_POPULATION;
	}
	
	

}
