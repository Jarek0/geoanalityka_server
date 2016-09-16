package pl.gisexpert.cms.admin.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DualListModel;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.cms.service.AccountService;

@Named
@ViewScoped
public class EditUserController implements Serializable {
    private static final long serialVersionUID = 1497269795945890070L;

    private Account account;
    
    @Inject
    RoleRepository roleRepository;
    
    @Inject
    AccountRepository accountRepository;
    
    @Inject
    AccountService accountService;
    
    private DualListModel<Role> roles;
    
    private String newPassword;
    private String newEmail;

    public void init(){
        
        Set<Role> rolesSource = new HashSet<>(roleRepository.findAll());        
        Set<Role> rolesTarget = accountService.getRoles(account);

        rolesSource.removeAll(rolesTarget);
        
        roles = new DualListModel<>(new ArrayList<>(rolesSource), new ArrayList<>(rolesTarget));
        
        newEmail = account.getEmailAddress();
        
    }
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    public void save(){
        
        if (newPassword != null && !newPassword.isEmpty()){
            account.setPassword(account.hashPassword(newPassword));
        }
        if (!newEmail.equals(account.getEmailAddress())){
            account.setEmailAddress(newEmail);
        }
      
        accountService.setRoles(account, new HashSet<>(roles.getTarget()));
    
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Parametry konta zostały zaktualizowane.");
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
    
    public DualListModel<Role> getRoles() {
        return roles;
    }

    public void setRoles(DualListModel<Role> roles) {
        this.roles = roles;
    }
  
    
}
