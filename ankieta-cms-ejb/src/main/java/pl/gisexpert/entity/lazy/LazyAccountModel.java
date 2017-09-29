package pl.gisexpert.entity.lazy;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;

public class LazyAccountModel extends LazyDataModel<Account> {
    private static final long serialVersionUID = -6386191158302101407L;

    @Inject
    private AccountRepository accountRepository;
    
    public LazyAccountModel(AccountRepository accountFacade){
        this.accountRepository = accountFacade;
    }

    @Override
    public List<Account> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        
        List<Account> result = accountRepository.loadLazy(first, pageSize, sortField, sortOrder, filters);
        int rowCount = accountRepository.count(filters);
        setRowCount(rowCount);
        return result;
    }

    @Override
    public Account getRowData(String rowKey) {
        Account account = accountRepository.find(Long.parseLong(rowKey));
        return account;
    }

    @Override
    public Object getRowKey(Account account) {
        return account.getId();
    }    
}
