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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
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
import org.slf4j.Logger;

import pl.gisexpert.cms.data.AccessTokenRepository;
import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.AddressRepository;
import pl.gisexpert.cms.data.CompanyRepository;
import pl.gisexpert.cms.data.LoginAttemptRepository;
import pl.gisexpert.cms.model.AccessToken;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.AccountConfirmation;
import pl.gisexpert.cms.model.AccountStatus;
import pl.gisexpert.cms.model.Address;
import pl.gisexpert.cms.model.Company;
import pl.gisexpert.cms.model.CompanyAccount;
import pl.gisexpert.cms.model.LoginAttempt;
import pl.gisexpert.cms.model.NaturalPersonAccount;
import pl.gisexpert.cms.model.PremiumPlanType;
import pl.gisexpert.cms.service.AccountService;
import pl.gisexpert.cms.service.LoginAttemptService;
import pl.gisexpert.cms.service.PremiumPlanService;
import pl.gisexpert.cms.visitor.AccountAddressVisitor;
import pl.gisexpert.cms.visitor.DefaultAccountVisitor;
import pl.gisexpert.rest.model.AccountInfo;
import pl.gisexpert.rest.model.AddressForm;
import pl.gisexpert.rest.model.BaseResponse;
import pl.gisexpert.rest.model.CompanyAddressForm;
import pl.gisexpert.rest.model.ContactInfo;
import pl.gisexpert.rest.model.GetTokenForm;
import pl.gisexpert.rest.model.GetTokenResponse;
import pl.gisexpert.rest.model.RegisterForm;
import pl.gisexpert.rest.model.RegisterResponse;
import pl.gisexpert.rest.util.producer.qualifier.RESTI18n;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.service.MailService;

@Path("/auth")
@RequestScoped
public class AuthRESTService {

	@Inject
	private AccountRepository accountRepository;
	
	@Inject
	private AccountService accountService;

	@Inject
	private AccessTokenRepository accessTokenRepository;
	
	@Inject
	private LoginAttemptRepository loginAttemptRepository;

	@Inject
	private LoginAttemptService loginAttemptService;
	
	@Inject
	private GlobalConfigService appConfig;

	@Inject
	private MailService mailService;
	
	@Inject
	private PremiumPlanService premiumPlanService;
	
	@Inject
	private CompanyRepository companyRepository;
	
	@Inject
	private AddressRepository addressRepository;
	
	@Inject
	private AccountAddressVisitor addressVisitor;
	
	@Inject
	@RESTI18n
	private ResourceBundle i18n;
	
	@Inject
	private Logger log;

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerAccount(@Context HttpServletRequest request, RegisterForm formData) {
		
		Account account;
		if (!formData.getNaturalPerson()) {
			account = new CompanyAccount();
		}
		else {
			account = new NaturalPersonAccount();
		}
		
		account.setUsername(formData.getEmail());
		account.setPassword(account.hashPassword(formData.getPassword()));
		account.setEmailAddress(formData.getEmail());
		account.setFirstName(formData.getFirstname());
		account.setLastName(formData.getLastname());
		if (formData.getQueuedPayment() != null) {
			account.setQueuedPayment(formData.getQueuedPayment());
		}
		
		final AddressForm addressForm = formData.getAddress();
		final Address address = new Address();
		
		address.setCity(addressForm.getCity());
		address.setStreet(addressForm.getStreet());
		address.setHouseNumber(addressForm.getBuildingNumber());
		address.setFlatNumber(addressForm.getFlatNumber());
		address.setZipcode(addressForm.getZipCode());

		if (!formData.getNaturalPerson()) {
			final CompanyAddressForm companyData = (CompanyAddressForm) addressForm;

			account.accept(new DefaultAccountVisitor(){
				@Override
				public void visit(CompanyAccount companyAccount) {
					Company company = new Company();
					company.setCompanyName(companyData.getCompanyName());
					company.setTaxId(companyData.getTaxId());
					company.setAddress(address);
					companyRepository.create(company);
					companyAccount.setCompany(company);
				}
			});			
		}
		else {
			account.accept(new DefaultAccountVisitor() {
				@Override
				public void visit(NaturalPersonAccount naturalAccount) {
					addressRepository.create(address);
					naturalAccount.setAddress(address);
					naturalAccount.setPhone(addressForm.getPhone());			
				}
			});
				
		}

		account.setDateRegistered(new Date());
		account.setAccountStatus(AccountStatus.UNCONFIRMED);

		UUID confirmationCode = UUID.randomUUID();
		AccountConfirmation accountConfirmation = new AccountConfirmation();
		accountConfirmation.setToken(confirmationCode.toString());

		account.setAccountConfirmation(accountConfirmation);

		try {
			accountRepository.create(account);
		} catch (Exception e) {

			BaseResponse errorStatus = new BaseResponse();
			errorStatus.setMessage("Invalid parameters.");
			errorStatus.setResponseStatus(Status.BAD_REQUEST);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorStatus).build();
		}
		
		premiumPlanService.activatePlan(account, PremiumPlanType.PLAN_DEDYKOWANY);

		String subject = "Geoanalizy - potwierdzenie rejestracji użytkownika";

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
		registerStatus.setMessage("Account created. Confirmation link has been sent to your E-Mail address. Use it to complete the registration.");
		registerStatus.setResponseStatus(Status.OK);
		registerStatus.setUsername(account.getUsername());
		registerStatus.setEmail(account.getEmailAddress());

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
			requestStatus.setMessage("Account confirmed successfully.");
			requestStatus.setResponseStatus(Response.Status.OK);

			account.setAccountConfirmation(null);
			account.setAccountStatus(AccountStatus.CONFIRMED);
			accountRepository.edit(account);

			try {
				
				String remoteHost = request.getHeader("Host");
				if (remoteHost == null){
					remoteHost = request.getRemoteHost();
				}
				
				return Response.temporaryRedirect(new URI(request.getScheme() + "://" + remoteHost
						+ appConfig.getClients().getLandingPageUrl() + "/login.html?activate_success=true")).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		requestStatus.setMessage("Account confirmation failed.");
		requestStatus.setResponseStatus(Response.Status.UNAUTHORIZED);

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
			rs.setMessage( i18n.getString("account.validation.usernamenotexists"));
			rs.setResponseStatus(Response.Status.UNAUTHORIZED);
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		if (account.getAccountStatus() == AccountStatus.UNCONFIRMED) {
			rs.setMessage(i18n.getString("account.validation.notconfirmed"));
			rs.setResponseStatus(Response.Status.UNAUTHORIZED);
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		if (account.getAccountStatus() == AccountStatus.DISABLED) {
			rs.setMessage(i18n.getString("account.validation.disabled"));
			rs.setResponseStatus(Response.Status.UNAUTHORIZED);
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}
		
		List<LoginAttempt> recentLoginAttempts = loginAttemptService.findRecentLoginAttempts(5, account, 100);
		if (recentLoginAttempts != null && recentLoginAttempts.size() == 20) {
			rs.setMessage(i18n.getString("account.validation.toomanyloginattempts"));
			rs.setResponseStatus(Response.Status.FORBIDDEN);
			return Response.status(Response.Status.FORBIDDEN).entity(rs).build();
		}
		
		LoginAttempt loginAttempt = new LoginAttempt();
		loginAttempt.setDate(new Date());
		loginAttempt.setAccount(account);

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

			Date date = new Date();
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MINUTE, 240); // token will expire after 240 minutes
			date = cal.getTime();

			accessToken.setExpires(date);
			accessToken.setAccount(account);
			accessToken = accessTokenRepository.create(accessToken,true);

			GetTokenResponse getTokenStatus = new GetTokenResponse();
			getTokenStatus.setMessage("Successfully generated token");
			getTokenStatus.setToken(token);
			getTokenStatus.setResponseStatus(Response.Status.OK);
			getTokenStatus.setExpires(date);
			
			loginAttempt.setSuccessful(true);
			account.setLastLoginDate(new Date());
			accountRepository.edit(account);
			loginAttemptRepository.create(loginAttempt);
			
			return Response.status(Response.Status.OK).entity(getTokenStatus).build();
		}
		
		loginAttempt.setSuccessful(false);
		loginAttemptRepository.create(loginAttempt);

		rs.setMessage(i18n.getString("account.validation.authfailed"));
		rs.setResponseStatus(Response.Status.UNAUTHORIZED);

		return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
	}
	
	@GET
	@Path("/renewToken")
	@Produces(MediaType.APPLICATION_JSON)
	public Response renewToken(@Context HttpServletRequest request) {
		String token = request.getHeader("Access-Token");
		
		AccessToken accessToken = accessTokenRepository.findByToken(token);
		
		if (accessToken == null) {
			log.debug("Failed to renew token: " + token);
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, 240); // token will expire after 240 minutes
		date = cal.getTime();

		accessToken.setExpires(date);
		accessTokenRepository.edit(accessToken);
		
		log.debug("Successfully renewed token: " + accessToken.getToken());
		
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
		
		if (username == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		log.debug("Getting account info for username: " + username);
		Account account = accountRepository.findByUsername(username);
		
		if (account == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		account = accountRepository.fetchContactData(account);
		
		AccountInfo accountInfo = new AccountInfo(account, new ArrayList<>(accountService.getRoles(account)));
		
		String token = request.getHeader("Access-Token");
		
		AccessToken accessToken = accessTokenRepository.findByToken(token);
		if (accessToken == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		accountInfo.setAccessToken(accessToken.getToken());
		accountInfo.setTokenExpires(accessToken.getExpires());

		return Response.status(Response.Status.OK).entity(accountInfo).build();
	}
	
	@GET
	@Path("/contactInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContactInfo() {

		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		
		if (username == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		Account account = accountRepository.findByUsername(username);
		account = accountRepository.fetchContactData(account);
		
		account.accept(addressVisitor);
		
		Address address = addressVisitor.getAddress();
		Company company = addressVisitor.getCompany();
		
		ContactInfo contactInfo;
		if (company != null) {
			contactInfo = new ContactInfo(company);
		}
		else if (address != null) {
			contactInfo = new ContactInfo(address);
			contactInfo.setPhone(account.getPhone());
		}
		else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		return Response.status(Response.Status.OK).entity(contactInfo).build();
	}
	
	@POST
	@Path("/contactInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeContactInfo(final ContactInfo contactInfo) {
		
		Subject currentUser = SecurityUtils.getSubject();
		String username = (String) currentUser.getPrincipal();
		
		if (username == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Account account = accountRepository.findByUsername(username);
		
		final Address address = new Address();
		address.setCity(contactInfo.getCity());
		address.setFlatNumber(contactInfo.getFlatNumber());
		address.setHouseNumber(contactInfo.getHouseNumber());
		address.setStreet(contactInfo.getStreet());
		address.setZipcode(contactInfo.getZipcode());
		
		account.accept(new DefaultAccountVisitor() {
			@Override
			public void visit(CompanyAccount account) {
				Company company = new Company();
				company.setCompanyName(contactInfo.getCompanyName());
				company.setTaxId(contactInfo.getTaxId());
				company.setPhone(contactInfo.getPhone());
				company.setAddress(address);
				
				companyRepository.create(company);
				account.setCompany(company);
			}
			@Override
			public void visit(NaturalPersonAccount account) {
				
				addressRepository.create(address);
				account.setPhone(contactInfo.getPhone());
				account.setAddress(address);
			}
		});
		
		accountRepository.edit(account);
		
		BaseResponse response = new BaseResponse();
		response.setMessage("Pomyślnie zmieniono dane.");
		response.setResponseStatus(Response.Status.OK);
		
		return Response.status(Response.Status.OK).entity(response).build();
	}
}
