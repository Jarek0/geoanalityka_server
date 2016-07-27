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
package pl.gisexpert.rest;


import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.rest.model.BaseResponse;

@Path("/validation")
public class ValidationRESTService {
	
	@Inject
	AccountRepository accountRepository;

	@GET
	@Path("/username_available")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsernameAvailable(@QueryParam("email") String username) {

		Account account = accountRepository.findByUsername(username);
		
		ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
		Response.Status status;
		
		BaseResponse requestStatus = new BaseResponse();
		if (account == null){
			status = Response.Status.OK;
			requestStatus.setMessage(i18n.getString("account.validation.usernameavailable"));
			requestStatus.setResponseStatus(Response.Status.OK);	
		}
		else {
			status = Response.Status.NOT_FOUND;
			requestStatus.setMessage(i18n.getString("account.validation.usernameunavailable"));
			requestStatus.setResponseStatus(Response.Status.NOT_FOUND);
		}
	
		
		return Response.status(status).entity(requestStatus).build();
	}
	
	@GET
	@Path("/username_exists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsernameExists(@QueryParam("email") String username) {

		Account account = accountRepository.findByUsername(username);

		ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
		Response.Status status;
		
		BaseResponse requestStatus = new BaseResponse();
		if (account != null){
			status = Response.Status.OK;
			requestStatus.setMessage(i18n.getString("account.validation.usernameexists"));
			requestStatus.setResponseStatus(Response.Status.OK);
		}
		else {
			status = Response.Status.NOT_FOUND;
			requestStatus.setMessage(i18n.getString("account.validation.usernamenotexists"));
			requestStatus.setResponseStatus(Response.Status.NOT_FOUND);
		}
	
		
		return Response.status(status).entity(requestStatus).build();
	}

}
