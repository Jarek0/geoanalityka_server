package  pl.gisexpert.cms.account.controller;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;

@Named
@ViewScoped
public class DeleteAccountController implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(DeleteAccountController.class);
    private static final long serialVersionUID = -5742015628185704799L;

    private Long accountId;
    
    @Inject
    private AccountRepository accountFacade;

    public void delete() {
        Account accountToBeDeleted = accountFacade.find(accountId);
        accountFacade.remove(accountToBeDeleted);
        log.info("Account with username: " + accountToBeDeleted.getUsername() + " has been deleted.");
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

}
