package pl.gisexpert.validation;

import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;

@ManagedBean(name = "emailUnicityValidator")
@RequestScoped
public class EmailUnicityValidator implements Validator {

    @Inject
    private AccountRepository accountFacade;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            FacesMessage message = new FacesMessage("Należy podać adres E-Mail.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }

        Account account = accountFacade.findByEmail((String) value);
        if (account != null) {
            
            FacesMessage message = new FacesMessage("Użytkownik o podanym adresie E-Mail już istnieje..");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    
            if (component.getAttributes().get("userId") != null) {
                
                Long userId = (Long) component.getAttributes().get("userId");
                
                if (!Objects.equals(userId, account.getId())) {
                    throw new ValidatorException(message);
                }
            }
            else {
                throw new ValidatorException(message);
            }
        }
    }
}
