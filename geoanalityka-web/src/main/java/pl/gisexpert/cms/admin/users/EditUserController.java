package pl.gisexpert.cms.admin.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DualListModel;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.AddressRepository;
import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.*;
import pl.gisexpert.cms.service.AccountService;
import pl.gisexpert.service.PasswordHasher;

@Named
@ViewScoped
@lombok.Getter
@lombok.Setter
public class EditUserController implements Serializable {
    private static final long serialVersionUID = 1497269795945890070L;

    private Account account;

    private Address address;

    private boolean isAdmin;

    @Inject
    AddressRepository addressRepository;

    @Inject
    PasswordHasher passwordHasher;
    
    @Inject
    RoleRepository roleRepository;
    
    @Inject
    AccountRepository accountRepository;
    
    @Inject
    AccountService accountService;
    
    private DualListModel<Role> roles;
    
    private String newPassword;
    private String newEmail;
    private String newFirstname;
    private String newLastname;
    private String newPhone;
    private String newZipCode;
    private String newCity;
    private String newStreet;
    private String newHouseNumber;
    private String newFlatNumber;

    private String accountStatus;

    public void init(){
        
        Set<Role> rolesSource = new HashSet<>(roleRepository.findAll());        
        Set<Role> rolesTarget = accountService.getRoles(account);

        rolesSource.removeAll(rolesTarget);
        
        roles = new DualListModel<>(new ArrayList<>(rolesSource), new ArrayList<>(rolesTarget));

        isAdmin=account.getRoles().stream().map(Role::getName).anyMatch(roleName -> roleName.equals("Administrator"));

        newEmail = account.getUsername();
        newFirstname = account.getFirstName();
        newLastname = account.getLastName();
        newPhone = account.getPhone();
        accountStatus = account.getAccountStatus().name();

        if(account instanceof NaturalPersonAccount) {
            address = accountRepository.findAddressByUsername(account.getUsername());
            newZipCode = address.getZipcode();
            newCity = address.getCity();
            newStreet = address.getStreet();
            newHouseNumber = address.getHouseNumber();
            newFlatNumber = address.getFlatNumber();
        }
    }
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void save(){
        
        if (newPassword != null && !newPassword.isEmpty()){
            account.setPassword(passwordHasher.hashPassword(newPassword));
        }

        account.setUsername(newEmail);
        account.setFirstName(newFirstname);
        account.setLastName(newLastname);
        account.setPhone(newPhone);
        if(!isAdmin)
        account.setAccountStatus(AccountStatus.valueOf(accountStatus));

        if(account instanceof NaturalPersonAccount) {
            address.setZipcode(newZipCode);
            address.setCity(newCity);
            address.setStreet(newStreet);
            address.setHouseNumber(newHouseNumber);
            address.setFlatNumber(newFlatNumber);
            ((NaturalPersonAccount) account).setAddress(address);
        }

        accountRepository.edit(account);
        if(roles.getTarget().stream().map(Role::getName).anyMatch(roleName -> roleName.equals("Administrator")))
            account.setAccountStatus(AccountStatus.VERIFIED);
        accountService.setRoles(account, new HashSet<>(roles.getTarget()));
    
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Parametry konta zostały zaktualizowane.");
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public AccountStatus[] getAllAccountStatus() {
        return AccountStatus.values();
    }

    public boolean getIsAdmin(){
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
}
