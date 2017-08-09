package pl.gisexpert.cms.admin.users;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.AccountStatus;
import pl.gisexpert.entity.lazy.LazyAccountModel;

@Named
@ViewScoped
public class BrowseUsersController implements Serializable {
    private static final long serialVersionUID = -5659287827597771123L;

    @Inject
    AccountRepository accountRepository;

    private LazyDataModel<Account> lazyAccounts;

    @PostConstruct
    public void setupLazyAccounts() {
        lazyAccounts = new LazyAccountModel(accountRepository);
    }

    public LazyDataModel<Account> getLazyAccounts() {
        return lazyAccounts;
    }

    public void setLazyAccounts(LazyDataModel<Account> lazyAccounts) {
        this.lazyAccounts = lazyAccounts;
    }
    
}
