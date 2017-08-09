package pl.gisexpert.validation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "firstnameUnicityValidator")
@RequestScoped
public class FirstNameUnicityValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            FacesMessage message = new FacesMessage("Należy podać imię.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }

        String firstname=((String) value);
        if (!firstname.matches("(^[A-Z ÀÁÂÃÄÅ ĄŻŹ ÒÓÔÕÖØ Ł Ć ĘŚ Ń ÈÉÊË Ç ÌÍÎÏ ÙÚÛÜ Ñ ]{1})([a-zàáâãäåąźżòóÓłćęśńôõöøèéêëçìíîïùúûüÿñ]{2,29}$)")) {

            FacesMessage message = new FacesMessage("Imię musi zaczynać się z dużej litery i mieć od 3 do 30 znaków");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
