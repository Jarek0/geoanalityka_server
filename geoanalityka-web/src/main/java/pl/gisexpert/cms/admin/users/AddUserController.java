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
import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.AccountStatus;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.service.GlobalConfigService;

@Named
@ViewScoped
public class AddUserController implements Serializable {
    private static final long serialVersionUID = -6814734174025620514L;

    Logger log = LoggerFactory.getLogger(AddUserController.class);
    
    private Account account;
    
    @Inject
    private RoleRepository roleRepository;
    
    @Inject
    private AccountRepository accountRepository;
    
    @Inject
    private GlobalConfigService appConfig;
    
    private DualListModel<Role> roles;
    
    public void init(){
        
        account = new Account();
        List<Role> rolesSource = roleRepository.findAll();
        List<Role> rolesTarget = new ArrayList<>();
        
        roles = new DualListModel<>(rolesSource, rolesTarget);
    }
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    public void add(){
        
        account.setDateRegistered(new Date());
        account.setRoles(new HashSet<>(roles.getTarget()));
        
        account.setPassword(account.hashPassword(account.getPassword()));
        account.setAccountStatus(AccountStatus.CONFIRMED);
        
        Integer baseCredits = appConfig.getSettings().getBaseCredits();
        baseCredits = baseCredits == null ? 100 : baseCredits;
        account.setCredits(baseCredits.doubleValue());
        
        accountRepository.create(account);
        
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
