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
package pl.gisexpert.rest.analysis;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.common.base.Joiner;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.analysis.DemographicAnalysisRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.analysis.AnalysisStatus;
import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;
import pl.gisexpert.cms.model.analysis.demographic.SimpleDemographicAnalysis;
import pl.gisexpert.cms.service.AccountService;
import pl.gisexpert.cms.service.AnalysisService;
import pl.gisexpert.rest.model.BaseResponse;
import pl.gisexpert.rest.model.analysis.*;
import pl.gisexpert.rest.model.analysis.demographic.SumAllInRadiusForm;
import pl.gisexpert.rest.model.analysis.demographic.SumRangeInRadiusForm;
import pl.gisexpert.rest.util.producer.qualifier.RESTI18n;
import pl.gisexpert.service.AnalysisCostCalculator;
import pl.gisexpert.stat.service.AddressStatService;

@Path("/analysis/demographic")
public class DemographicAnalysisRESTService {

	@Inject
	private AddressStatService addressStatRepository;

	@Inject
	private AccountRepository accountRepository;

	@Inject
	private AccountService accountService;
	
	@Inject
	private AnalysisService analysisService;

	@Inject
	private DemographicAnalysisRepository analysisRepository;

	@Inject
	private AnalysisCostCalculator analysisCostCalculator;

	@Inject
	@RESTI18n
	private ResourceBundle i18n;


	
	private final int MIN_VALID_POPULATION = 50;

	/**
	 * Returns total population in given radius around given point
	 * 
	 * @param sumAllInRadiusForm
	 * @return
	 */
	@POST
	@Path("/sum_total")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sumTotal(SumAllInRadiusForm sumAllInRadiusForm) {

		SimpleDemographicAnalysis analysis = new SimpleDemographicAnalysis();
		Account creator = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

		if (accountService.hasRole(creator, "PLAN_TESTOWY") && sumAllInRadiusForm.getRadius() > 2000) {
			BaseResponse response = new BaseResponse();
			response.setResponseStatus(Response.Status.NOT_FOUND);
			response.setMessage("Analizowanie obszaru o promieniu większym niż 2 km możliwe jest w planie standardowym lub wyższym.");
			return Response.status(Response.Status.NOT_FOUND).entity(response).build();
		}
		
		analysis.setCreator(creator);
		analysis.setDateStarted(new Date());
		analysis.setRadius(sumAllInRadiusForm.getRadius());
		analysis.setLocation(sumAllInRadiusForm.getPoint());
		analysis.setLocationDisplayName(sumAllInRadiusForm.getLocationName());
		analysis.setName(sumAllInRadiusForm.getName());
		analysis.setStatus(AnalysisStatus.PENDING);
		
		Double analysisCost = analysisCostCalculator.calculate(analysis);
		if (creator.getCredits() < analysisCost) {
			BaseResponse response = new BaseResponse();
			response.setResponseStatus(Response.Status.UNAUTHORIZED);
			response.setMessage("Insufficient credits");
			return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
		}

		Integer totalPopulation = addressStatRepository.sumAllInRadius(sumAllInRadiusForm.getRadius(),
				sumAllInRadiusForm.getPoint());
		Integer inhabitedPremises = addressStatRepository.sumAllPremisesInRadius(sumAllInRadiusForm.getRadius(),
				sumAllInRadiusForm.getPoint());
		
		if (!isPopulationHighEnough(totalPopulation)) {
			BaseResponse response = new BaseResponse();
			response.setResponseStatus(Response.Status.NOT_FOUND);
			
			Object[] messageArguments = { MIN_VALID_POPULATION };
			String pattern = i18n.getString("analysis.demographic.lowpopulation");
			MessageFormat formatter = new MessageFormat(pattern);
		    formatter.setLocale(i18n.getLocale());		    
			response.setMessage(formatter.format(messageArguments));
			
			return Response.status(Response.Status.NOT_FOUND).entity(response).build();
		}

		analysis.setPopulation(totalPopulation);
		analysis.setInhabitedPremises(inhabitedPremises);

		analysis.setDateFinished(new Date());
		analysis.setStatus(AnalysisStatus.FINISHED);

		analysisRepository.create(analysis);
		analysis = (SimpleDemographicAnalysis) analysisService.addDemographicAnalysis(creator, analysis);

		creator.setCredits(creator.getCredits() - analysisCost);
		accountRepository.edit(creator);

		AnalysisHashResponse responseValue = new AnalysisHashResponse();
		responseValue.setResponseStatus(Response.Status.OK);
		responseValue.setHash(analysis.getHash().toString());

		return Response.status(Response.Status.OK).entity(responseValue).build();
	}

	/**
	 * Performs analysis on population in given radius, around a given point and
	 * also in given age range. Result contains two dictionaries containing
	 * population of men and women, divided into small age ranges inside the
	 * given age range
	 * 
	 * @param sumRangeInRadiusForm
	 * @return
	 */
	@POST
	@Path("/sum_range")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sumRange(SumRangeInRadiusForm sumRangeInRadiusForm) {

		AdvancedDemographicAnalysis analysis = new AdvancedDemographicAnalysis();
		Account creator = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

		if (accountService.hasRole(creator, "PLAN_TESTOWY") && sumRangeInRadiusForm.getRadius() > 2000) {
			BaseResponse response = new BaseResponse();
			response.setResponseStatus(Response.Status.NOT_FOUND);
			response.setMessage("Analizowanie obszaru o promieniu większym niż 2 km możliwe jest w planie standardowym lub wyższym.");
			return Response.status(Response.Status.NOT_FOUND).entity(response).build();
		}
		
		analysis.setCreator(creator);
		analysis.setDateStarted(new Date());
		analysis.setRadius(sumRangeInRadiusForm.getRadius());
		analysis.setLocation(sumRangeInRadiusForm.getPoint());
		analysis.setLocationDisplayName(sumRangeInRadiusForm.getLocationName());
		analysis.setName(sumRangeInRadiusForm.getName());
		analysis.setStatus(AnalysisStatus.PENDING);

		Integer[] range = sumRangeInRadiusForm.getRange();
		analysis.setAgeRange(Joiner.on("-").join(range));

		Double analysisCost = analysisCostCalculator.calculate(analysis);
		if (creator.getCredits() < analysisCost) {
			BaseResponse response = new BaseResponse();
			response.setResponseStatus(Response.Status.UNAUTHORIZED);
			response.setMessage("Insufficient credits");
			return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
		}

		HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges = addressStatRepository
				.sumRangeInRadius(range, sumRangeInRadiusForm.getRadius(), sumRangeInRadiusForm.getPoint());
		
		if (!isPopulationHighEnough(kobietyAndMezczyzniByAgeRanges)) {
			BaseResponse response = new BaseResponse();
			response.setResponseStatus(Response.Status.NOT_FOUND);
			
			Object[] messageArguments = { MIN_VALID_POPULATION };
			String pattern = i18n.getString("analysis.demographic.lowpopulation");
			MessageFormat formatter = new MessageFormat(pattern);
		    formatter.setLocale(i18n.getLocale());		    
			response.setMessage(formatter.format(messageArguments));
			
			return Response.status(Response.Status.NOT_FOUND).entity(response).build();
		}
	
		analysis.setKobietyAndMezczyzniByAgeRanges(kobietyAndMezczyzniByAgeRanges);
		
		Integer inhabitedPremises = addressStatRepository.sumAllPremisesInRadius(sumRangeInRadiusForm.getRadius(),
				sumRangeInRadiusForm.getPoint());
		
		analysis.setInhabitedPremises(inhabitedPremises);

		analysis.setDateFinished(new Date());
		analysis.setStatus(AnalysisStatus.FINISHED);

		analysisRepository.create(analysis);
		analysis = (AdvancedDemographicAnalysis) analysisService.addDemographicAnalysis(creator, analysis);

		creator.setCredits(creator.getCredits() - analysisCost);
		accountRepository.edit(creator);

		AnalysisHashResponse responseValue = new AnalysisHashResponse();
		responseValue.setHash(analysis.getHash().toString());
		responseValue.setResponseStatus(Response.Status.OK);

		return Response.status(Response.Status.OK).entity(responseValue).build();
	}

	@POST
	@Path("/calculate_cost/sum_total")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calculateSumTotalCost(SumAllInRadiusForm sumAllInRadiusForm) {

		SimpleDemographicAnalysis analysis = new SimpleDemographicAnalysis();
		analysis.setRadius(sumAllInRadiusForm.getRadius());

		Double cost = analysisCostCalculator.calculate(analysis);

		BaseResponse response = new BaseResponse();
		response.setMessage(String.valueOf(cost.intValue()));
		response.setResponseStatus(Response.Status.OK);

		return Response.status(Response.Status.OK).entity(response).build();
	}

	@POST
	@Path("/calculate_cost/sum_range")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calculateSumAllInRadiusCost(SumRangeInRadiusForm sumRangeInRadiusForm) {

		AdvancedDemographicAnalysis analysis = new AdvancedDemographicAnalysis();
		Integer[] range = sumRangeInRadiusForm.getRange();
		analysis.setAgeRange(Joiner.on("-").join(range));
		analysis.setRadius(sumRangeInRadiusForm.getRadius());

		Double cost = analysisCostCalculator.calculate(analysis);

		BaseResponse response = new BaseResponse();
		response.setMessage(String.valueOf(cost.intValue()));
		response.setResponseStatus(Response.Status.OK);

		return Response.status(Response.Status.OK).entity(response).build();
	}

	/*@GET
	@Path("/recent/{begin}/{end}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecentAnalyses(@PathParam("begin") Integer begin, @PathParam("end") Integer end) {

		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		Account account = accountRepository.findByUsername(username);

		List<DemographicAnalysis> accountAnalyses = analysisRepository.findMostRecentRangeForAccount(account, begin, end);


		List<DemographicAnalysisDetails> analysesDetailsList = new ArrayList<>();
		
		for (DemographicAnalysis analysis : accountAnalyses) {
			analysesDetailsList.add(new DemographicAnalysisDetails(analysis));
		}

		return Response.status(Response.Status.OK).entity(analysesDetailsList).build();
	}*/

	@GET
	@Path("/recent/{begin}/{end}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecentAnalysesByType(@PathParam("begin") Integer begin, @PathParam("end") Integer end, @QueryParam("cp") Integer page, @QueryParam("ps") Integer pageSize, @QueryParam("ordBy") String orderBy, @QueryParam("type") List<String> type){

		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		Account account = accountRepository.findByUsername(username);
		List<DemographicAnalysis> accountAnalyses;
		PaginatedAnalysesDetailList paginatedAnalysesDetailList = new PaginatedAnalysesDetailList();


		if(type.size()>0) {
			accountAnalyses = analysisRepository.findMostRecentRangeAndTypeForAccount(account, begin, end, orderBy, getAnalysisClassType(type));
			paginatedAnalysesDetailList.setNumberOfPages((int) Math.ceil(accountAnalyses.size()/(double)pageSize));
			paginatedAnalysesDetailList.setPageSize(pageSize);
			paginatedAnalysesDetailList.setNumberOfAnalyses(accountAnalyses.size());
			begin = (page - 1) * pageSize;
			end = begin + pageSize;
			accountAnalyses = analysisRepository.findMostRecentRangeAndTypeForAccount(account, begin, end, orderBy,  getAnalysisClassType(type));
		}
		else
			accountAnalyses = analysisRepository.findMostRecentRangeForAccount(account, begin, end);

		List<DemographicAnalysisDetails> analysesDetailsList = new ArrayList<>();

		for (DemographicAnalysis analysis : accountAnalyses) {
			analysesDetailsList.add(new DemographicAnalysisDetails(analysis));
		}

		paginatedAnalysesDetailList.setAnalysesDetailsList(analysesDetailsList);

		return Response.status(Response.Status.OK).entity(paginatedAnalysesDetailList).build();
	}

	@GET
	@Path("/details/{hash}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response analysisDetails(@PathParam("hash") String hash) {

		DemographicAnalysis analysis = analysisRepository.findByHash(UUID.fromString(hash));
		DemographicAnalysisDetails details = null;
		
		if (analysis instanceof AdvancedDemographicAnalysis) {
			details = new AdvancedDemographicAnalysisDetails(
					(AdvancedDemographicAnalysis) analysis);
		} else if (analysis instanceof SimpleDemographicAnalysis) {
			details = new SimpleDemographicAnalysisDetails(
					(SimpleDemographicAnalysis) analysis);
		}

		if (analysis != null) {
			return Response.status(Response.Status.OK).entity(details).build();
		} else {
			BaseResponse response = new BaseResponse();
			response.setMessage("Analysis not found");
			response.setResponseStatus(Response.Status.NOT_FOUND);
			return Response.status(Response.Status.NOT_FOUND).entity(response).build();
		}
	}

	@GET
	@Path("/recent/clearRecentHistory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response clearRecentHistory(@QueryParam("hashes") List<String> hash){

		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		Account account = accountRepository.findByUsername(username);

		analysisRepository.setDeletedStatusForRecentsByHashesForAccount(account, hash);

		BaseResponse response = new BaseResponse();
		response.setResponseStatus(Response.Status.OK);
		response.setMessage("Wyczyszczono historię pomyślnie");

		return Response.status(Response.Status.OK).entity(response).build();
	}

	@GET
	@Path("/new_analysis_name")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAnalysisName(@QueryParam("name") String name,@QueryParam("hash") String hash){

		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		Account account = accountRepository.findByUsername(username);

		if( name == null || name.length()==0)
		{
			name = "Bez nazwy";
		}
		analysisRepository.updateAnalysisNameForAccount(account, name, hash);

		BaseResponse response = new BaseResponse();
		response.setResponseStatus(Response.Status.OK);
		response.setMessage("Nazwa została poprawnie zmieniona");

		return Response.status(Response.Status.OK).entity(response).build();
	}

	private List<Class> getAnalysisClassType(List<String> types) {

		List<Class> analysisTypes = new ArrayList<>();

		for (String type: types) {
			switch (type) {
				case "SimpleDemographicAnalysis":
					analysisTypes.add(SimpleDemographicAnalysis.class);
					break;
				case "AdvancedDemographicAnalysis":
					analysisTypes.add(AdvancedDemographicAnalysis.class);
					break;
			}
		}

		return analysisTypes;
	}
	
	public boolean isPopulationHighEnough(int population) {
		return population > MIN_VALID_POPULATION;
	}
	
	public boolean isPopulationHighEnough(HashMap<String, HashMap<Integer, Integer>> kobietyAndMezczyzniByAgeRanges) {
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

}
