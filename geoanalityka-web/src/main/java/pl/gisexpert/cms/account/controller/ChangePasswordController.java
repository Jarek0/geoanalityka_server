
package  pl.gisexpert.cms.account.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;

@Named
@RequestScoped
public class ChangePasswordController {
    
    @Inject
    private AccountController accountController;
    
    @Inject
    private AccountRepository accountRapository;
    
    private String currentPassword;
    
    private String newPassword;
    
    public void changePassword(){
        
        DefaultPasswordService passwordService = new DefaultPasswordService();
        DefaultHashService dhs = new DefaultHashService();
        dhs.setHashIterations(100000);
        dhs.setHashAlgorithmName("SHA-256");
        passwordService.setHashService(dhs);
        
        Account account = accountController.getAccount();
        
        FacesContext context = FacesContext.getCurrentInstance();
        if (passwordService.passwordsMatch(currentPassword, account.getPassword())){
            account.setPassword(passwordService.encryptPassword(newPassword));
            accountRapository.edit(account);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Hasło zostało zmienione", null);
            context.addMessage(null, message);
        }
        else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nieprawidłowe hasło", null);
            context.addMessage(null, message);
        }
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public AccountController getAccountController() {
        return accountController;
    }

    public void setAccountController(AccountController accountController) {
        this.accountController = accountController;
    }
    
}
