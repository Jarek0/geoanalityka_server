package pl.gisexpert.cms.admin.users;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.CompanyRepository;
import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.*;
import pl.gisexpert.rest.model.BaseResponse;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.service.MailService;

@Named
@ViewScoped
public class AddUserController implements Serializable {
    private static final long serialVersionUID = -6814734174025620514L;

    Logger log = LoggerFactory.getLogger(AddUserController.class);
    
    private CompanyAccount account;
    private Company company;
    
    @Inject
    private RoleRepository roleRepository;
    
    @Inject
    private AccountRepository accountRepository;
    
    @Inject
    private CompanyRepository companyRepository;
    
    @Inject
    private GlobalConfigService appConfig;

    @Inject
    private MailService mailService;
    
    private DualListModel<Role> roles;
    
    public void init(){
        
        account = new CompanyAccount();
        Address address = new Address();
        address.setCity("Testowe Miasto");
        address.setHouseNumber("1");
        address.setStreet("Testowa Ulica");
        address.setZipcode("00-000");

        company = new Company();
        company.setAddress(address);
        company.setPhone("000000000");
        company.setTaxId("000-00-00-000");
        account.setAccountStatus(AccountStatus.UNCONFIRMED);
        
        List<Role> rolesSource = roleRepository.findAll();
        List<Role> rolesTarget = new ArrayList<>();
        
        roles = new DualListModel<>(rolesSource, rolesTarget);
    }
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(CompanyAccount account) {
        this.account = account;
    }
    
    public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void add(){
        String tempPassword = GenerateTempPassword().toString();
    	account.setUsername(account.getEmailAddress());
        
        account.setDateRegistered(new Date());

        account.setPassword(account.hashPassword(tempPassword));
        account.setAccountStatus(AccountStatus.UNCONFIRMED);
        
        Integer baseCredits = appConfig.getSettings().getBaseCredits();
        baseCredits = baseCredits == null ? 100 : baseCredits;
        account.setCredits(baseCredits.doubleValue());
        
        companyRepository.create(company);
        account.setCompany(company);

        UUID confirmationCode = UUID.randomUUID();
        AccountConfirmation accountConfirmation = new AccountConfirmation();
        accountConfirmation.setToken(confirmationCode.toString());

        account.setAccountConfirmation(accountConfirmation);
        accountRepository.create(account);
        account.setRoles(new HashSet<>(roles.getTarget()));
        accountRepository.edit(account);

        String subject = "Geoanalizy.pl - potwierdzenie rejestracji użytkownika";
        MessageFormat formatter = new MessageFormat("");
        ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
        formatter.setLocale(i18n.getLocale());
        formatter.applyPattern(i18n.getString("account.confirm.emailtextwithpassword"));
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath();
        String confirmAccountURL = baseURL + "/rest/auth/confirm/" + account.getId() + "/" + confirmationCode;
        Object[] params = { confirmAccountURL, account.getEmailAddress(), tempPassword };

        String emailText = formatter.format(params);
        mailService.sendMail(subject, emailText, account.getEmailAddress());
        
        log.info("Created account with username: " + account.getUsername());
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Konto zostało utworzone");
        FacesContext.getCurrentInstance().addMessage("addUser", msg);
    }

    public DualListModel<Role> getRoles() {
        return roles;
    }

    public void setRoles(DualListModel<Role> roles) {
        this.roles = roles;
    }

    private StringBuilder GenerateTempPassword(){
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb;
    }
  
    
}
