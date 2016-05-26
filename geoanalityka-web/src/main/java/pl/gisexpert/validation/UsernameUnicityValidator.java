package pl.gisexpert.validation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import pl.gisexpert.cms.data.AccountRepository;

@ManagedBean(name = "usernameUnicityValidator")
@RequestScoped
public class UsernameUnicityValidator implements Validator {
	
    @Inject
    private AccountRepository accountFacade;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value==null) {
            FacesMessage message = new FacesMessage("Nazwa użytkownika nie może być pusta.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }
        if (accountFacade.findByUsername((String)value) != null) {
            FacesMessage message = new FacesMessage("Użytkownik o podanej nazwie już istnieje.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
       }
    }
}
