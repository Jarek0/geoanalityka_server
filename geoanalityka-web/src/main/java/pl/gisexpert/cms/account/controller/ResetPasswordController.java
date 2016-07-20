package pl.gisexpert.cms.account.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.hibernate.validator.constraints.Email;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.ResetPassword;
import pl.gisexpert.cms.util.producer.qualifier.I18n;
import pl.gisexpert.service.MailService;
import pl.gisexpert.util.RandomTokenGenerator;

@Named
@ViewScoped
public class ResetPasswordController implements Serializable {
	private static final long serialVersionUID = -6484884920307007267L;

	@Email
	private String email;

	@Inject
	private MailService mailService;
	
	@Inject
	@I18n
	private transient ResourceBundle i18n;

	private String resetPasswordToken;
	private Boolean tokenValid;
	private String newPassword;

	@Inject
	private AccountRepository accountFacade;

	private Account account;

	private final RandomTokenGenerator resetPasswordTokenGenerator = new RandomTokenGenerator();

	public void resetPassword() {

		String subject = i18n.getString("account.resetpassword.emailtitle");
		FacesContext context = Faces.getContext();

		Account account = accountFacade.findByEmail(email);
		if (account != null) {

			String token = resetPasswordTokenGenerator.nextToken();
			ResetPassword resetPassword = new ResetPassword();
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR, 24);
			date = cal.getTime();

			resetPassword.setExpDate(date);
			resetPassword.setToken(token);

			account.setResetPassword(resetPassword);
			accountFacade.edit(account);

			MessageFormat formatter = new MessageFormat("");
			formatter.setLocale(i18n.getLocale());

			formatter.applyPattern(i18n.getString("account.resetpassword.emailtext"));
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			String url = request.getRequestURL().toString();
			String baseURL = url.substring(0, url.length() - request.getRequestURI().length())
					+ request.getContextPath();

			String resetPasswordURL = baseURL + "/reset_password/" + token;
			Object[] params = { resetPasswordURL };
			String emailText = formatter.format(params);

			mailService.sendMail(subject, emailText, email);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Wiadomość została wysłana", null);
			context.addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Podany adres E-Mail nie jest zarejestrowany w systemie", null);
			context.addMessage(null, message);
		}

	}

	public void lookupToken() {

		FacesContext context = FacesContext.getCurrentInstance();

		if (resetPasswordToken != null) {
			account = accountFacade.findByResetPasswordToken(resetPasswordToken);
			if (account != null && account.getResetPassword().getExpDate().after(new Date())) {
				tokenValid = true;
				return;
			}

			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			FacesMessage failMessgae = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Podany link jest już nieaktualny lub nieprawidłowy. Proszę ponownie skorzystać z formularza.",
					null);
			context.addMessage("resetPassword", failMessgae);
		}

	}

	public String changePassword() {

		DefaultPasswordService passwordService = new DefaultPasswordService();
		DefaultHashService dhs = new DefaultHashService();
		dhs.setHashIterations(100000);
		dhs.setHashAlgorithmName("SHA-256");
		passwordService.setHashService(dhs);

		FacesContext context = FacesContext.getCurrentInstance();
		account.setPassword(passwordService.encryptPassword(newPassword));
		account.setResetPassword(new ResetPassword());
		accountFacade.edit(account);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Hasło zostało zmienione", null);

		context.addMessage(null, message);
		context.getExternalContext().getFlash().setKeepMessages(true);
		return "/faces/login.xhtml?faces-redirect=true";

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public Boolean getTokenValid() {
		return tokenValid;
	}

	public void setTokenValid(Boolean tokenValid) {
		this.tokenValid = tokenValid;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
