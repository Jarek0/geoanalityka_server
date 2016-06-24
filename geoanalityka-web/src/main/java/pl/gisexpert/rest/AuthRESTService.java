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

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.subject.Subject;

import pl.gisexpert.cms.data.AccessTokenRepository;
import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.AccessToken;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.AccountConfirmation;
import pl.gisexpert.cms.model.AccountStatus;
import pl.gisexpert.cms.model.Address;
import pl.gisexpert.cms.service.BearerTokenService;
import pl.gisexpert.rest.model.AccountInfo;
import pl.gisexpert.rest.model.BaseResponse;
import pl.gisexpert.rest.model.GetTokenForm;
import pl.gisexpert.rest.model.GetTokenResponse;
import pl.gisexpert.rest.model.RegisterForm;
import pl.gisexpert.rest.model.RegisterResponse;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.service.MailService;

@Path("/auth")
public class AuthRESTService {

	@Inject
	AccountRepository accountRepository;

	@Inject
	BearerTokenService tokenService;

	@Inject
	AccessTokenRepository accessTokenRepository;

	@Inject
	GlobalConfigService appConfig;

	@Inject
	MailService mailService;

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerAccount(@Context HttpServletRequest request, RegisterForm formData) {

		Account account = new Account();
		account.setUsername(formData.getEmail());
		account.setPassword(account.hashPassword(formData.getPassword()));
		account.setEmailAddress(formData.getEmail());

		Address address = new Address();
		address.setFirstName(formData.getFirstname());
		address.setLastName(formData.getLastname());
		address.setCity(formData.getCompanyAddress().getCity());
		address.setCompanyName(formData.getCompanyAddress().getCompanyName());
		address.setTaxId(formData.getCompanyAddress().getTaxId());
		address.setStreet(formData.getCompanyAddress().getStreet());
		address.setHouseNumber(formData.getCompanyAddress().getBuildingNumber());
		address.setFlatNumber(formData.getCompanyAddress().getFlatNumber());
		address.setZipcode(formData.getCompanyAddress().getZipCode());

		account.setAddress(address);

		account.setDateRegistered(new Date());
		account.setAccountStatus(AccountStatus.UNCONFIRMED);

		UUID confirmationCode = UUID.randomUUID();
		AccountConfirmation accountConfirmation = new AccountConfirmation();
		accountConfirmation.setToken(confirmationCode.toString());

		account.setAccountConfirmation(accountConfirmation);

		try {
			accountRepository.create(account);
		} catch (Exception e) {

			System.out.println(e.getMessage());

			BaseResponse errorStatus = new BaseResponse();
			errorStatus.message = "Invalid parameters.";
			errorStatus.responseStatus = Status.BAD_REQUEST;
			return Response.status(Response.Status.BAD_REQUEST).entity(errorStatus).build();
		}

		String subject = "Geoanalityka - potwierdzenie rejestracji użytkownika";

		MessageFormat formatter = new MessageFormat("");

		ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
		formatter.setLocale(i18n.getLocale());

		formatter.applyPattern(i18n.getString("account.confirm.emailtext"));

		String url = request.getRequestURL().toString();
		String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();

		String confirmAccountURL = baseURL + "/rest/auth/confirm/" + account.getId() + "/" + confirmationCode;
		Object[] params = { confirmAccountURL };

		String emailText = formatter.format(params);
		mailService.sendMail(subject, emailText, account.getEmailAddress());

		RegisterResponse registerStatus = new RegisterResponse();
		registerStatus.message = "Account created. Confirmation link has been sent to your E-Mail address. Use it to complete the registration.";
		registerStatus.responseStatus = Status.OK;
		registerStatus.username = account.getUsername();
		registerStatus.email = account.getEmailAddress();

		return Response.status(Response.Status.OK).entity(registerStatus).build();
	}

	@GET
	@Path("/confirm/{accountId}/{confirmationToken}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmAccount(@Context HttpServletRequest request, @PathParam("accountId") Long accountId,
			@PathParam("confirmationToken") String confirmationToken) {

		Account account = accountRepository.find(accountId);

		BaseResponse requestStatus = new BaseResponse();

		if (account.getAccountConfirmation().getToken().equals(confirmationToken)) {
			requestStatus.message = "Account confirmed successfully.";
			requestStatus.responseStatus = Response.Status.OK;

			account.setAccountConfirmation(null);
			account.setAccountStatus(AccountStatus.CONFIRMED);
			accountRepository.edit(account);

			try {
				return Response.temporaryRedirect(new URI(request.getScheme() + "://" + request.getRemoteHost()
						+ appConfig.getLandingPageUrl() + "/login.html?activate_success=true")).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		requestStatus.message = "Account confirmation failed.";
		requestStatus.responseStatus = Response.Status.UNAUTHORIZED;

		return Response.status(Response.Status.UNAUTHORIZED).entity(requestStatus).build();
	}

	@POST
	@Path("/getToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getToken(GetTokenForm formData) {

		Account account = accountRepository.findByUsername(formData.getUsername());

		BaseResponse rs = new BaseResponse();
		if (account == null) {
			rs.message = "Account not found";
			rs.responseStatus = Response.Status.UNAUTHORIZED;
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		if (account.getAccountStatus() == AccountStatus.UNCONFIRMED) {
			rs.message = "You need to confirm your registration first";
			rs.responseStatus = Response.Status.UNAUTHORIZED;
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		if (account.getAccountStatus() == AccountStatus.DISABLED) {
			rs.message = "Your account has been disabled";
			rs.responseStatus = Response.Status.UNAUTHORIZED;
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		DefaultPasswordService passwordService = new DefaultPasswordService();
		DefaultHashService dhs = new DefaultHashService();
		dhs.setHashIterations(5);
		passwordService.setHashService(dhs);

		if (passwordService.passwordsMatch(formData.getPassword(), account.getPassword())) {

			AccessToken accessToken = new AccessToken();
			String token = UUID.randomUUID().toString();

			while (accessTokenRepository.findByToken(token) != null) {
				token = UUID.randomUUID().toString();
			}
			accessToken.setToken(token);
			accessToken.setAccount(account);

			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, 240); // token will expire after 240 minutes
			date = cal.getTime();

			accessToken.setExpires(date);

			accessTokenRepository.create(accessToken);
			account = tokenService.addToken(account, accessToken);

			GetTokenResponse getTokenStatus = new GetTokenResponse();
			getTokenStatus.message = "Successfully generated token";
			getTokenStatus.token = token;
			getTokenStatus.responseStatus = Response.Status.OK;
			getTokenStatus.expires = date;

			return Response.status(Response.Status.OK).entity(getTokenStatus).build();
		}

		rs.message = "Authentication failed.";
		rs.responseStatus = Response.Status.UNAUTHORIZED;

		return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
	}
	
	@GET
	@Path("/renewToken")
	@Produces(MediaType.APPLICATION_JSON)
	public Response renewToken(@Context HttpServletRequest request) {
		String token = request.getHeader("Access-Token");
		
		AccessToken accessToken = accessTokenRepository.findByToken(token);
		if (accessToken == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, 240); // token will expire after 240 minutes
		date = cal.getTime();

		accessToken.setExpires(date);
		
		return Response.status(Response.Status.OK).build();
		
	}

	@GET
	@Path("/logout")
	public Response deauthToken(@Context HttpServletRequest request) {

		String token = request.getHeader("Access-Token");

		if (token == null) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		AccessToken tokenEntity = accessTokenRepository.findByToken(token);
		tokenEntity.setExpires(new Date());
		accessTokenRepository.edit(tokenEntity);

		SecurityUtils.getSubject().logout();

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/accountInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountInfo(@Context HttpServletRequest request) {

		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();

		Account account = accountRepository.findByUsername(username, true);

		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setUsername(username);
		accountInfo.setEmail(account.getEmailAddress());
		accountInfo.setLastLogin(account.getLastLoginDate());
		accountInfo.setCredits(account.getCredits());
		
		String token = request.getHeader("Access-Token");
		
		AccessToken accessToken = accessTokenRepository.findByToken(token);
		if (accessToken == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		accountInfo.setAccessToken(accessToken.getToken());
		accountInfo.setTokenExpires(accessToken.getExpires());

		Address address = account.getAddress();
		accountInfo.setFirstName(address.getFirstName());
		accountInfo.setLastName(address.getLastName());
		accountInfo.setCompanyName(address.getCompanyName());

		return Response.status(Response.Status.OK).entity(accountInfo).build();
	}
}
