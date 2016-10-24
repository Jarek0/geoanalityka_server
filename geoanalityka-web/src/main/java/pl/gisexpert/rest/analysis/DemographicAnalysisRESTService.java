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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;

import com.google.common.base.Joiner;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.analysis.DemographicAnalysisRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.analysis.AreaType;
import pl.gisexpert.cms.model.analysis.TravelType;
import pl.gisexpert.cms.model.analysis.demographic.AdvancedDemographicAnalysis;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysisBuilder;
import pl.gisexpert.cms.model.analysis.demographic.SimpleDemographicAnalysis;
import pl.gisexpert.cms.service.AccountService;
import pl.gisexpert.cms.service.AnalysisCostCalculator;
import pl.gisexpert.cms.service.AnalysisService;
import pl.gisexpert.model.gis.Coordinate;
import pl.gisexpert.rest.model.BaseResponse;
import pl.gisexpert.rest.model.analysis.AdvancedDemographicAnalysisDetails;
import pl.gisexpert.rest.model.analysis.AnalysisHashResponse;
import pl.gisexpert.rest.model.analysis.AnalysisStatusResponse;
import pl.gisexpert.rest.model.analysis.DemographicAnalysisDetails;
import pl.gisexpert.rest.model.analysis.PaginatedAnalysesDetailList;
import pl.gisexpert.rest.model.analysis.SimpleDemographicAnalysisDetails;
import pl.gisexpert.rest.model.analysis.demographic.AdvancedAnalysisForm;
import pl.gisexpert.rest.model.analysis.demographic.SimpleAnalysisForm;
import pl.gisexpert.rest.util.producer.qualifier.RESTI18n;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.stat.service.AsyncAnalysisService;

@Path("/analysis/demographic")
@Stateless
public class DemographicAnalysisRESTService {

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private AccountService accountService;

    @Inject
    private DemographicAnalysisRepository analysisRepository;

    @Inject
    private AnalysisService analysisService;

    @Inject
    private AnalysisCostCalculator analysisCostCalculator;

    @Inject
    private AsyncAnalysisService asyncAnalysisService;

    @Inject
    private Logger log;

    @Inject
    private GlobalConfigService appConfig;

    @Inject
    @RESTI18n
    private ResourceBundle i18n;

    /**
     * Returns total population in given area around given point
     *
     * @param simpleAnalysisForm
     * @return
     */
    @POST
    @Path("/simple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response simpleAnalysis(SimpleAnalysisForm simpleAnalysisForm) {

        Account creator = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

        if (accountService.hasRole(creator, "PLAN_TESTOWY")) {
            if (simpleAnalysisForm.getRadius() > 2000) {
                BaseResponse response = new BaseResponse();
                response.setResponseStatus(Response.Status.NOT_FOUND);
                response.setMessage(
                        "Analizowanie obszaru o promieniu większym niż 2 km możliwe jest w planie standardowym lub wyższym.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
            ArrayList<ArrayList<Double>> bBox = appConfig.getPlanTestowyBbox().getBbox();
            Double xMin, xMax, yMin, yMax;
            Coordinate analysisPoint = simpleAnalysisForm.getPoint();
            xMin = bBox.get(0).get(1);
            xMax = bBox.get(1).get(1);
            yMin = bBox.get(2).get(0);
            yMax = bBox.get(0).get(0);
            if (!(analysisPoint.getX() > xMin && analysisPoint.getX() < xMax && analysisPoint.getY() > yMin && analysisPoint.getY() < yMax)) {
                BaseResponse response = new BaseResponse();
                response.setResponseStatus(Response.Status.NOT_FOUND);
                response.setMessage(
                        "Analiza może zostać wykonana tylko w wyznaczonym obszarze");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
		}
        
        Coordinate location = simpleAnalysisForm.getPoint();


        String locationDisplayName = simpleAnalysisForm.getLocationName();
        String analysisName = simpleAnalysisForm.getName();
        SimpleDemographicAnalysis analysis;

        switch (simpleAnalysisForm.getAreaType()) {
            case RADIUS:
                Integer radius = simpleAnalysisForm.getRadius();
                analysis = (SimpleDemographicAnalysis) (DemographicAnalysisBuilder.simple().areaType(AreaType.RADIUS)
                        .radius(radius).creator(creator).name(analysisName).location(location)
                        .locationDisplayName(locationDisplayName).build());
                break;
            case TRAVEL_TIME:
            default:
                if (!accountService.hasRole(creator, "PLAN_ZAAWANSOWANY")
                        && !accountService.hasRole(creator, "PLAN_DEDYKOWANY")) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }

                Integer travelTime = simpleAnalysisForm.getTravelTimeOrDistance();
                TravelType travelType = simpleAnalysisForm.getTravelType();
                analysis = (SimpleDemographicAnalysis) (DemographicAnalysisBuilder.simple().areaType(AreaType.TRAVEL_TIME)
                        .travelTimeOrDistance(travelTime).travelType(travelType).location(location)
                        .locationDisplayName(locationDisplayName).name(analysisName).build());
        }

        Double analysisCost = analysisCostCalculator.calculate(analysis);
        if (creator.getCredits() < analysisCost) {
            BaseResponse response = new BaseResponse();
            response.setResponseStatus(Response.Status.UNAUTHORIZED);
            response.setMessage("Insufficient credits");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }

        analysis.setHash(analysisRepository.nextAnalysisHash().toString());

        creator.setCredits(creator.getCredits() - analysisCostCalculator.calculate(analysis));
        accountRepository.edit(creator);

        asyncAnalysisService.executeSimpleDemographicAnalysis(analysis);

        SimpleDemographicAnalysisDetails analysisDetails = new SimpleDemographicAnalysisDetails(analysis);
        return Response.status(Response.Status.OK).entity(analysisDetails).build();
    }

    /**
     * Sum population by age ranges around a given point (by radius or travel
     * time) Result consists of two dictionaries containing population of men
     * and women, divided into 5-year age ranges inside the given age range
     *
     * @param advancedAnalysisForm
     * @return
     */
    @POST
    @Path("/advanced")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response advancedAnalysis(AdvancedAnalysisForm advancedAnalysisForm) {

        Account creator = accountRepository.findByUsername((String) SecurityUtils.getSubject().getPrincipal());

        if (accountService.hasRole(creator, "PLAN_TESTOWY")) {
            if (advancedAnalysisForm.getRadius() > 2000) {
                BaseResponse response = new BaseResponse();
                response.setResponseStatus(Response.Status.NOT_FOUND);
                response.setMessage(
                        "Analizowanie obszaru o promieniu większym niż 2 km możliwe jest w planie standardowym lub wyższym.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
            ArrayList<ArrayList<Double>> bBox = appConfig.getPlanTestowyBbox().getBbox();
            Double xMin, xMax, yMin, yMax;
            Coordinate analysisPoint = advancedAnalysisForm.getPoint();
            xMin = bBox.get(0).get(1);
            xMax = bBox.get(1).get(1);
            yMin = bBox.get(2).get(0);
            yMax = bBox.get(0).get(0);
            if (!(analysisPoint.getX() > xMin && analysisPoint.getX() < xMax && analysisPoint.getY() > yMin && analysisPoint.getY() < yMax)) {
                BaseResponse response = new BaseResponse();
                response.setResponseStatus(Response.Status.NOT_FOUND);
                response.setMessage(
                        "Analiza może zostać wykonana tylko w wyznaczonym obszarze");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        }

        Integer[] ageRange = advancedAnalysisForm.getRange();
        String ageRangeStr = Joiner.on("-").join(ageRange);
        Coordinate location = advancedAnalysisForm.getPoint();
        String locationDisplayName = advancedAnalysisForm.getLocationName();
        String analysisName = advancedAnalysisForm.getName();

        AdvancedDemographicAnalysis analysis;
        switch (advancedAnalysisForm.getAreaType()) {
            case RADIUS:
                Integer radius = advancedAnalysisForm.getRadius();
                analysis = (AdvancedDemographicAnalysis) (DemographicAnalysisBuilder.advanced().areaType(AreaType.RADIUS)
                        .radius(radius).ageRange(ageRangeStr).location(location).locationDisplayName(locationDisplayName)
                        .creator(creator).name(analysisName).build());
                break;
            case TRAVEL_TIME:
            default:
                if (!accountService.hasRole(creator, "PLAN_ZAAWANSOWANY")
                        && !accountService.hasRole(creator, "PLAN_DEDYKOWANY")) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }

                Integer travelTimeOrDistance = advancedAnalysisForm.getTravelTimeOrDistance();
                TravelType travelType = advancedAnalysisForm.getTravelType();
                analysis = (AdvancedDemographicAnalysis) (DemographicAnalysisBuilder.advanced()
                        .areaType(AreaType.TRAVEL_TIME).travelTimeOrDistance(travelTimeOrDistance).travelType(travelType).ageRange(ageRangeStr)
                        .location(location).locationDisplayName(locationDisplayName).creator(creator).name(analysisName)
                        .build());
        }

        Double analysisCost = analysisCostCalculator.calculate(analysis);
        if (creator.getCredits() < analysisCost) {
            BaseResponse response = new BaseResponse();
            response.setResponseStatus(Response.Status.UNAUTHORIZED);
            response.setMessage("Insufficient credits");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }

        analysis.setHash(analysisRepository.nextAnalysisHash().toString());

        asyncAnalysisService.executeAdvancedDemographicAnalysis(analysis);

        creator.setCredits(creator.getCredits() - analysisCost);
        accountRepository.edit(creator);

        AdvancedDemographicAnalysisDetails analysisDetails = new AdvancedDemographicAnalysisDetails(analysis);
        return Response.status(Response.Status.OK).entity(analysisDetails).build();
    }

    @POST
    @Path("/calculate_cost/simple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateSimpleAnalysisCost(SimpleAnalysisForm simpleAnalysisForm) {

        SimpleDemographicAnalysis analysis = new SimpleDemographicAnalysis();
        analysis.setRadius(simpleAnalysisForm.getRadius());

        Double cost = analysisCostCalculator.calculate(analysis);

        BaseResponse response = new BaseResponse();
        response.setMessage(String.valueOf(cost.intValue()));
        response.setResponseStatus(Response.Status.OK);

        return Response.status(Response.Status.OK).entity(response).build();
    }

    @POST
    @Path("/calculate_cost/advanced")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateAdvancedAnalysisCost(AdvancedAnalysisForm advancedAnalysisForm) {

        AdvancedDemographicAnalysis analysis = new AdvancedDemographicAnalysis();
        Integer[] range = advancedAnalysisForm.getRange();
        analysis.setAgeRange(Joiner.on("-").join(range));
        analysis.setRadius(advancedAnalysisForm.getRadius());

        Double cost = analysisCostCalculator.calculate(analysis);

        BaseResponse response = new BaseResponse();
        response.setMessage(String.valueOf(cost.intValue()));
        response.setResponseStatus(Response.Status.OK);

        return Response.status(Response.Status.OK).entity(response).build();
    }

	/*
     * @GET
	 * 
	 * @Path("/recent/{begin}/{end}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 * getRecentAnalyses(@PathParam("begin") Integer begin, @PathParam("end")
	 * Integer end) {
	 * 
	 * Subject currentUser = SecurityUtils.getSubject(); String username =
	 * (String) currentUser.getPrincipal(); Account account =
	 * accountRepository.findByUsername(username);
	 * 
	 * List<DemographicAnalysis> accountAnalyses =
	 * analysisRepository.findMostRecentRangeForAccount(account, begin, end);
	 * 
	 * 
	 * List<DemographicAnalysisDetails> analysesDetailsList = new ArrayList<>();
	 * 
	 * for (DemographicAnalysis analysis : accountAnalyses) {
	 * analysesDetailsList.add(new DemographicAnalysisDetails(analysis)); }
	 * 
	 * return
	 * Response.status(Response.Status.OK).entity(analysesDetailsList).build();
	 * }
	 */

    @GET
    @Path("/compare")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComparedAnalyses(@QueryParam("hashes") List<String> hashes, @QueryParam("analysisType") String type) {

        Subject currentUser = SecurityUtils.getSubject();
        String username = (String) currentUser.getPrincipal();
        Account account = accountRepository.findByUsername(username);

        List<DemographicAnalysis> accountAnalyses = analysisRepository.getAnalysesDetailsToCompareForAccount(account, hashes);


        List<DemographicAnalysisDetails> analysesDetailsList = new ArrayList<>();


        for (DemographicAnalysis analysis : accountAnalyses) {
            if (analysis instanceof AdvancedDemographicAnalysis) {
                analysesDetailsList.add(new AdvancedDemographicAnalysisDetails(
                        (AdvancedDemographicAnalysis) analysis));
            } else if (analysis instanceof SimpleDemographicAnalysis) {
                analysesDetailsList.add(new SimpleDemographicAnalysisDetails(
                        (SimpleDemographicAnalysis) analysis));
            }

        }

        return Response.status(Response.Status.OK).entity(analysesDetailsList).build();
    }

    @GET
    @Path("/recent/{begin}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecentAnalysesByType(@PathParam("begin") Integer begin, @PathParam("end") Integer end,
                                            @QueryParam("cp") Integer page, @QueryParam("ps") Integer pageSize, @QueryParam("ordBy") String orderBy,
                                            @QueryParam("type") List<String> type) {

        Subject currentUser = SecurityUtils.getSubject();
        String username = (String) currentUser.getPrincipal();
        Account account = accountRepository.findByUsername(username);
        List<DemographicAnalysis> accountAnalyses;
        PaginatedAnalysesDetailList paginatedAnalysesDetailList = new PaginatedAnalysesDetailList();

        if (type.size() > 0) {
            accountAnalyses = analysisRepository.findMostRecentRangeAndTypeForAccount(account, begin, end, orderBy,
                    getAnalysisClassType(type));
            paginatedAnalysesDetailList.setNumberOfPages((int) Math.ceil(accountAnalyses.size() / (double) pageSize));
            paginatedAnalysesDetailList.setPageSize(pageSize);
            paginatedAnalysesDetailList.setNumberOfAnalyses(accountAnalyses.size());
            begin = (page - 1) * pageSize;
            end = begin + pageSize;
            accountAnalyses = analysisRepository.findMostRecentRangeAndTypeForAccount(account, begin, end, orderBy,
                    getAnalysisClassType(type));
        } else {
            accountAnalyses = analysisRepository.findMostRecentRangeForAccount(account, begin, end);
        }

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
            details = new AdvancedDemographicAnalysisDetails((AdvancedDemographicAnalysis) analysis);
        } else if (analysis instanceof SimpleDemographicAnalysis) {
            details = new SimpleDemographicAnalysisDetails((SimpleDemographicAnalysis) analysis);
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
    @Path("/status/{hash}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response analysisStatus(@PathParam("hash") String hash) {

        DemographicAnalysis analysis = analysisRepository.findByHash(UUID.fromString(hash));
        if (analysis != null) {
            AnalysisStatusResponse response = new AnalysisStatusResponse();
            response.setStatus(analysis.getStatus());
            response.setStatusCode(analysis.getStatusCode());
            response.setResponseStatus(Response.Status.OK);
            return Response.status(Response.Status.OK).entity(response).build();
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
    public Response clearRecentHistory(@QueryParam("hashes") List<String> hash) {

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
    public Response updateAnalysisName(@QueryParam("name") String name, @QueryParam("hash") String hash) {

        Subject currentUser = SecurityUtils.getSubject();
        String username = (String) currentUser.getPrincipal();
        Account account = accountRepository.findByUsername(username);

        if (name == null || name.length() == 0) {
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

        for (String type : types) {
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

}
