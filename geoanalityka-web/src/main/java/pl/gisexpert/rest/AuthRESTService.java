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

import java.text.MessageFormat;
import java.util.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
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

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;

import pl.gisexpert.cms.data.*;
import pl.gisexpert.cms.model.*;
import pl.gisexpert.cms.service.AccountService;
import pl.gisexpert.cms.service.LoginAttemptService;
import pl.gisexpert.rest.Validator.RegistrationValidator;
import pl.gisexpert.rest.model.*;
import pl.gisexpert.rest.util.producer.qualifier.RESTI18n;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.service.MailService;
import pl.gisexpert.service.PasswordHasher;
import pl.gisexpert.util.RandomTokenGenerator;

import static org.apache.shiro.authc.credential.DefaultPasswordService.DEFAULT_HASH_ALGORITHM;

@Path("/auth")
@RequestScoped
public class AuthRESTService {

    @Inject
	private AccountRepository accountRepository;

    @Inject
	private RoleRepository roleRepository;

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
	private AddressRepository addressRepository;

    @Inject
	private PasswordHasher passwordHasher;

    @Inject
	private RegistrationValidator registrationValidator;

	@RESTI18n
	private ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");

    @Inject
	private Logger log;

	private final RandomTokenGenerator resetPasswordTokenGenerator = new RandomTokenGenerator();

	private MessageFormat formatter = new MessageFormat("");

	public AuthRESTService(){
		formatter.setLocale(i18n.getLocale());
	}

	@POST
	@Path("/resendMail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response resendVerificationMail(@Context HttpServletRequest request, String username){
		Account account = accountRepository.findByEmail(username);
		if (account == null) {
			BaseResponse rs = new BaseResponse(Status.BAD_REQUEST,"Konto nie jest zarejestrowane w systemie");
			return Response.status(Response.Status.BAD_REQUEST).entity(rs).build();
		}

		UUID confirmationCode = UUID.randomUUID();
		AccountConfirmation accountConfirmation = new AccountConfirmation(confirmationCode);

		account.setAccountConfirmation(accountConfirmation);

		String baseURL = "http://localhost/aplikacja3d_bilgoraj/bilgoraj_v2?resetToken=";
		String confirmAccountURL = baseURL + "/rest/auth/confirm/" + confirmationCode;

		formatter.applyPattern(i18n.getString("account.confirm.emailtext"));
		Object[] params = { confirmAccountURL };

		String emailText = formatter.format(params);

		if(AccountStatus.UNCONFIRMED.equals(account.getAccountStatus())) {
			try {
				accountRepository.edit(account);
				mailService.sendMail(
						"Public Survey bilgoraj - potwierdzenie rejestracji użytkownika",
						emailText,
						username);
				BaseResponse successResponse = new BaseResponse(Status.OK,"Mail został wysłany ponownie");
				return Response.status(Response.Status.OK).entity(successResponse).build();
			} catch (Exception e) {
				BaseResponse errorStatus = new BaseResponse(Status.BAD_REQUEST,"Wysłanie maila nie powiodło się");
				return Response.status(Response.Status.BAD_REQUEST).entity(errorStatus).build();
			}
		}
		BaseResponse errorStatus = new BaseResponse(Status.BAD_REQUEST,"Twoje konto nie jest nie potwierdzone");
		return Response.status(Response.Status.BAD_REQUEST).entity(errorStatus).build();
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerAccount(@Context HttpServletRequest request, RegisterForm formData) {
		Map<String,String> errors = registrationValidator.validate(formData);
		if(errors.size()>0) {
			BaseResponse errorStatus = new BaseResponse(Status.BAD_REQUEST,(new Gson()).toJson(errors));
			return Response.status(Response.Status.BAD_REQUEST).entity(errorStatus).build();
		}

		final AddressForm addressForm = formData.getAddress();

		final Address address = new Address(addressForm.getZipCode(),
				addressForm.getCity(),
				addressForm.getStreet(),
				addressForm.getBuildingNumber(),
				addressForm.getFlatNumber());

		UUID confirmationCode = UUID.randomUUID();

		Role role=roleRepository.findByName("Ankietowani");

		Account account =
				new Account(formData.getUsername(),
						formData.getFirstname(),
						formData.getLastname(),
						passwordHasher.hashPassword(formData.getPassword()),
						addressForm.getPhone(),
						Sets.newHashSet(role),
						new Date(),
						AccountStatus.UNCONFIRMED,
						new AccountConfirmation(confirmationCode.toString()),
						address
				);

		role.addAccount(account);
		address.setAccount(account);

		try {
			accountRepository.create(account);
			roleRepository.edit(role);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			BaseResponse errorStatus = new BaseResponse(Status.INTERNAL_SERVER_ERROR,"Nastąpił nieoczekiwany błąd przy tworzeniu konta." +
					" Skontaktuj się z administratorem");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorStatus).build();
		}

		String subject = "Geoanalizy.pl - potwierdzenie rejestracji użytkownika";

		ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
		formatter.applyPattern(i18n.getString("account.confirm.emailtext"));
		String url = request.getRequestURL().toString();
		String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
		String confirmAccountURL = baseURL + "/rest/auth/confirm/" + confirmationCode;
		Object[] params = { confirmAccountURL };
		String emailText = formatter.format(params);

		try {
			mailService.sendMail(subject, emailText, account.getUsername());
		} catch (MessagingException e) {
			BaseResponse registerStatus = new BaseResponse(Status.OK,"Niestety wysyłanie maila weryfikacyjnego nie " +
					"powiodło się. Skontaktuj się z administratorem");
			return Response.status(Response.Status.OK).entity(registerStatus).build();
		}

		BaseResponse registerStatus = new BaseResponse(Status.OK,"Mail został wysłany ponownie");
		return Response.status(Response.Status.OK).entity(registerStatus).build();
	}

	@GET
	@Path("/confirm/{confirmationToken}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmAccount(@Context HttpServletRequest request,
								   @PathParam("confirmationToken") String confirmationToken) {

		Account account = accountRepository.findByToken(confirmationToken);

		if (!account.getAccountConfirmation().getToken().equals(confirmationToken)) {
			BaseResponse requestStatus = new BaseResponse(Response.Status.UNAUTHORIZED,"Weryfikacja konta nie powiodła się.");
			return Response.status(Response.Status.UNAUTHORIZED).entity(requestStatus).build();
		}

		account.setAccountConfirmation(null);
		account.setAccountStatus(AccountStatus.CONFIRMED);
		accountRepository.edit(account);

		String subject = "Public Survey bilgoraj - weryfikacja użytkownika";
		String emailText =new StringBuilder().append("Użytkownik: ")
				.append(account.getUsername()).append(" prosi o weryfikację danych przez administratora.").toString();

		List<String> adminUserNames=roleRepository.findAllAdminsUsernames();
		mailService.sendMail(subject, emailText, adminUserNames);

		BaseResponse requestStatus = new BaseResponse(Response.Status.OK,"Konto zostało zweryfikowane. " +
				"Twoje konto potrzebuje aktywacji przez administratora");
		return Response.status(Response.Status.OK).entity(requestStatus).build();
	}

	@POST
	@Path("/getToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getToken(@Context HttpServletRequest request, GetTokenForm formData) {

		Account account = accountRepository.findByEmail(formData.getUsername());

		if (account == null) {
			BaseResponse rs = new BaseResponse(Response.Status.UNAUTHORIZED,
					i18n.getString("account.validation.usernamenotexists"));
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		if (account.getAccountStatus() == AccountStatus.UNCONFIRMED) {
			BaseResponse rs = new BaseResponse(Response.Status.UNAUTHORIZED,
					i18n.getString("account.validation.notconfirmed"));
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		if (account.getAccountStatus() == AccountStatus.DISABLED) {
			BaseResponse rs = new BaseResponse(Response.Status.UNAUTHORIZED,
					i18n.getString("account.validation.disabled"));
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		if (account.getAccountStatus() == AccountStatus.CONFIRMED) {
			BaseResponse rs = new BaseResponse(Response.Status.UNAUTHORIZED,
					i18n.getString("account.validation.confirmed"));
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

		List<LoginAttempt> recentLoginAttempts = loginAttemptService.findRecentLoginAttempts(5, account, 100);
		if (recentLoginAttempts != null && recentLoginAttempts.size() == 20) {
			BaseResponse rs = new BaseResponse(Response.Status.FORBIDDEN,
					i18n.getString("account.validation.toomanyloginattempts"));
			return Response.status(Response.Status.FORBIDDEN).entity(rs).build();
		}

		LoginAttempt loginAttempt = new LoginAttempt();
		loginAttempt.setDate(new Date());
		loginAttempt.setAccount(account);
		loginAttempt.setIp(request.getRemoteAddr());

		DefaultPasswordService passwordService = new DefaultPasswordService();
		DefaultHashService dhs = new DefaultHashService();
		dhs.setHashIterations(5);
		dhs.setHashAlgorithmName(DEFAULT_HASH_ALGORITHM);
		passwordService.setHashService(dhs);

		if (!passwordService.passwordsMatch(formData.getPassword(), account.getPassword())) {
			loginAttempt.setSuccessful(false);
			loginAttemptRepository.create(loginAttempt);
			BaseResponse rs = new BaseResponse(Response.Status.UNAUTHORIZED,
					i18n.getString("account.validation.authfailed"));
			return Response.status(Response.Status.UNAUTHORIZED).entity(rs).build();
		}

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

		GetTokenResponse getTokenStatus =
				new GetTokenResponse(account.getFirstName(),
						account.getLastName(),
						accessToken.getToken(),
						date,Response.Status.OK,
						"Successfully generated token");

		loginAttempt.setSuccessful(true);
		account.setLastLoginDate(new Date());
		accountRepository.edit(account);
		loginAttemptRepository.create(loginAttempt);

		return Response.status(Response.Status.OK).entity(getTokenStatus).build();

	}

	@GET
	@Path("/renewToken")
	@Consumes(MediaType.APPLICATION_JSON)
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
		accessTokenRepository.edit(accessToken);

		log.debug("Udało się odświeżyć autentyfikację: " + accessToken.getToken());

		return Response.status(Response.Status.OK).build();

	}

	@POST
	@Path("/resetPassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPassword(@Context HttpServletRequest request, ResetPasswordForm resetPasswordForm) {

		String subject = i18n.getString("account.resetpassword.emailtitle");

		Account account = accountRepository.findByEmail(resetPasswordForm.getUsername());
		if (account == null) {
			BaseResponse rs = new BaseResponse(Status.BAD_REQUEST,"Podany adres E-Mail nie jest zarejestrowany w systemie");
			return Response.status(Response.Status.BAD_REQUEST).entity(rs).build();
		}

		String token = resetPasswordTokenGenerator.nextToken();
		Date expirationDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(expirationDate);
		cal.add(Calendar.HOUR, 24);
		expirationDate = cal.getTime();

		ResetPassword resetPassword = new ResetPassword(token, expirationDate);

		account.setResetPassword(resetPassword);
		accountRepository.edit(account);

		formatter.applyPattern(i18n.getString("account.resetpassword.emailtext"));

		String baseURL = "http://localhost/aplikacja3d_bilgoraj/bilgoraj_v2?resetToken=";
		String resetPasswordURL = baseURL + token;
		Object[] params = { resetPasswordURL };
		String emailText = formatter.format(params);

		try {
			mailService.sendMail(subject, emailText, resetPasswordForm.getUsername());
			BaseResponse rs = new BaseResponse(Status.OK,"Wiadomość została wysłana");
			return Response.status(Status.OK).entity(rs).build();
		} catch (Exception e) {
			BaseResponse rs = new BaseResponse(Status.BAD_REQUEST,"Nie udało się wysłać maila. W celu ponownego " +
					"wysłania spróbuj zalogować się w systemie lub skontaktuj się z administratorem");
			return Response.status(Response.Status.BAD_REQUEST).entity(rs).build();
		}
	}

	@POST
	@Path("/changePassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPassword(@Context HttpServletRequest request,ChangePasswordForm changePasswordForm) {

		DefaultPasswordService passwordService = new DefaultPasswordService();
		DefaultHashService dhs = new DefaultHashService();
		dhs.setHashIterations(5);
		dhs.setHashAlgorithmName("SHA-256");
		passwordService.setHashService(dhs);

		Account account = accountRepository.findByResetPasswordToken(changePasswordForm.getResetPasswordToken());

		if(account==null){
			BaseResponse rs = new BaseResponse(Status.BAD_REQUEST,"Nieprawidłowy token");
			return Response.status(Response.Status.BAD_REQUEST).entity(rs).build();
		}
		if(changePasswordForm.getConfirmPassword()==null || changePasswordForm.getPassword()==null ||
				changePasswordForm.getConfirmPassword().isEmpty() || changePasswordForm.getPassword().isEmpty()){
			BaseResponse rs = new BaseResponse(Status.BAD_REQUEST,"Pole nie może być puste");
			return Response.status(Response.Status.BAD_REQUEST).entity(rs).build();
		}
		if(!changePasswordForm.getConfirmPassword().equals(changePasswordForm.getPassword())){
			BaseResponse rs = new BaseResponse(Status.BAD_REQUEST,"Podane hasła nie są zgodne");
			return Response.status(Response.Status.BAD_REQUEST).entity(rs).build();
		}

		account.setPassword(passwordService.encryptPassword(changePasswordForm.getPassword()));
		account.setResetPassword(new ResetPassword());
		accountRepository.edit(account);

		BaseResponse rs = new BaseResponse(Status.OK,"Hasło zostało zmienione");
		return Response.status(Status.OK).entity(rs).build();
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

}