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

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.gisexpert.rest.model.analysis.demographic.SumAllInRadiusForm;
import pl.gisexpert.rest.model.analysis.demographic.SumAllInRadiusResponse;
import pl.gisexpert.rest.model.analysis.demographic.SumRangeInRadiusForm;
import pl.gisexpert.rest.model.analysis.demographic.SumRangeInRadiusResponse;
import pl.gisexpert.stat.data.AddressStatRepository;

@Path("/analysis/demographic")
public class DemographicAnalysisRESTService {

	@Inject
	AddressStatRepository addressStatRepository;

	/**
	 * Returns total population in given radius around given point
	 * @param sumAllInRadiusForm
	 * @return
	 */
	@POST
	@Path("/sum_total")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sumTotal(SumAllInRadiusForm sumAllInRadiusForm) {

		SumAllInRadiusResponse responseValue = new SumAllInRadiusResponse();
		Integer sum = addressStatRepository.sumAllInRadius(sumAllInRadiusForm.getRadius(),
				sumAllInRadiusForm.getPoint());
		responseValue.setSum(sum);
		return Response.status(Response.Status.OK).entity(responseValue).build();
	}

	/**
	 * Performs analysis on population in given radius, around a given point and also in given age range.
	 * Result contains two dictionaries containing population of men and women, divided into small
	 * age ranges inside the given age range
	 * @param sumRangeInRadiusForm
	 * @return
	 */
	@POST
	@Path("sum_range")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sumRange(SumRangeInRadiusForm sumRangeInRadiusForm) {

		Integer[] range = sumRangeInRadiusForm.getRange();

		Map<String, Map<Integer, Integer>> kobietyAndMezczyzniByAgeRanges = addressStatRepository
				.sumRangeInRadius(range, sumRangeInRadiusForm.getRadius(), sumRangeInRadiusForm.getPoint());

		SumRangeInRadiusResponse responseValue = new SumRangeInRadiusResponse();
		responseValue.setKobiety(kobietyAndMezczyzniByAgeRanges.get("kobiety"));
		responseValue.setMezczyzni(kobietyAndMezczyzniByAgeRanges.get("mezczyzni"));
		responseValue.responseStatus = Response.Status.OK;
		
		return Response.status(Response.Status.OK).entity(responseValue).build();
	}

}
