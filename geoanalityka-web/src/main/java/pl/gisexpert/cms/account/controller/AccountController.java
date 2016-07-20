package pl.gisexpert.cms.account.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;

@Named
@SessionScoped
public class AccountController implements Serializable {

	private static final long serialVersionUID = -2770826072475357001L;

	private Account account;

	@Inject
	private AccountRepository accountRepository;

	@Inject
	private Logger log;

	
	@PostConstruct
	public void init() {		
		account = new Account();
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			log.info((String) subject.getPrincipal());
			account = accountRepository.findByUsername((String) subject.getPrincipal());
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean getLoggedIn() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isAuthenticated();
	}

	public void logOut() {

		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {

			subject.logout();
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			// ec.invalidateSession();
			account = null;
			try {
				ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
