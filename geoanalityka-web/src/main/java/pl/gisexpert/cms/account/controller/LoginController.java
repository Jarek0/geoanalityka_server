package pl.gisexpert.cms.account.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.omnifaces.cdi.ViewScoped;
import org.slf4j.Logger;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.util.producer.qualifier.I18n;

@Named
@ViewScoped
public class LoginController implements Serializable {

	@Inject
	private Logger log;
	private static final long serialVersionUID = -7411102744396381176L;

	private Boolean rememberMe = false;

	@Inject
	private AccountRepository accountRepository;

	private String username;
	private String password;

	@Inject
	private AccountController accountController;

	@Inject
	@I18n
	private transient ResourceBundle i18n;

	@PostConstruct
	public void init() {
		log.info("Constructed");
	}

	public void logIn() {

		log.info("logIn call");
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		log.debug("Session id: " + ec.getSessionId(false));

		Subject subject = SecurityUtils.getSubject();

		if (subject.isAuthenticated()) {
			subject.logout();
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);

		log.info("Username: " + username + ", password: " + password);

		try {
			subject.login(token);
			Account account = accountRepository.findByUsername(username);
			this.accountController.setAccount(account);
			account.setLastLoginDate(new Date());
			accountRepository.edit(account);

			log.info("User " + username + " successfully logged in.");

			SavedRequest savedRequest = WebUtils.getAndClearSavedRequest((ServletRequest) ec.getRequest());
			try {
				ec.redirect(savedRequest != null ? savedRequest.getRequestUrl() : (ec.getRequestContextPath() + "/"));
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
			}

		} catch (UnknownAccountException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, i18n.getString("login.unknownaccount"), null));
			log.error(ex.getMessage(), ex);
		} catch (IncorrectCredentialsException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, i18n.getString("login.unknowncredentials"), null));
			log.error(ex.getMessage(), ex);
		} catch (AuthenticationException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, i18n.getString("login.unknownaccountorcredentials"), null));
			log.error(ex.getMessage(), ex);
		} finally {
			token.clear();
		}

	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public String getUsername() {
		log.info("Getting username: " + username);
		return username;
	}

	public void setUsername(String username) {
		log.info("Setting username: " + username);
		this.username = username;
	}

	public String getPassword() {
		log.info("Getting password: " + password);
		return password;
	}

	public void setPassword(String password) {
		log.info("Setting password: " + password);
		this.password = password;
	}

	public AccountController getAccountController() {
		return accountController;
	}

	public void setAccountController(AccountController accountController) {
		this.accountController = accountController;
	}

}
