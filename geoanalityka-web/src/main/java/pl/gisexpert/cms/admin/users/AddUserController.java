package pl.gisexpert.cms.admin.users;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.*;
import pl.gisexpert.service.GlobalConfigService;
import pl.gisexpert.service.MailService;

@Named
@ViewScoped
public class AddUserController implements Serializable {
    private static final long serialVersionUID = -6814734174025620514L;

    Logger log = LoggerFactory.getLogger(AddUserController.class);

    @Inject
    private RoleRepository roleRepository;
    
    @Inject
    private AccountRepository accountRepository;


    @Inject
    private GlobalConfigService appConfig;

    @Inject
    private MailService mailService;
    
    private DualListModel<Role> roles;
    
    public void init(){
        Address address = new Address();
        address.setCity("Testowe Miasto");
        address.setHouseNumber("1");
        address.setStreet("Testowa Ulica");
        address.setZipcode("00-000");

        List<Role> rolesSource = roleRepository.findAll();
        List<Role> rolesTarget = new ArrayList<>();
        
        roles = new DualListModel<>(rolesSource, rolesTarget);
    }

	public void add(){
        UUID confirmationCode = UUID.randomUUID();
        AccountConfirmation accountConfirmation = new AccountConfirmation();
        accountConfirmation.setToken(confirmationCode.toString());

        MessageFormat formatter = new MessageFormat("");
        ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
        formatter.setLocale(i18n.getLocale());
        formatter.applyPattern(i18n.getString("account.confirm.emailtextwithpassword"));
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
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
