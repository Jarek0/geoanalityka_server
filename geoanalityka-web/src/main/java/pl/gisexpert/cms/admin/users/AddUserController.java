package pl.gisexpert.cms.admin.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.CompanyRepository;
import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.AccountStatus;
import pl.gisexpert.cms.model.Address;
import pl.gisexpert.cms.model.Company;
import pl.gisexpert.cms.model.CompanyAccount;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.service.GlobalConfigService;

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
        account.setAccountStatus(AccountStatus.CONFIRMED);
        
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
    	
    	account.setUsername(account.getEmailAddress());
        
        account.setDateRegistered(new Date());
        
        account.setPassword(account.hashPassword(account.getPassword()));
        account.setAccountStatus(AccountStatus.CONFIRMED);
        
        Integer baseCredits = appConfig.getSettings().getBaseCredits();
        baseCredits = baseCredits == null ? 100 : baseCredits;
        account.setCredits(baseCredits.doubleValue());
        
        companyRepository.create(company);
        account.setCompany(company);
        accountRepository.create(account);
        account.setRoles(new HashSet<>(roles.getTarget()));
        accountRepository.edit(account);
        
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
  
    
}
