package pl.gisexpert.validation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "houseNumberUnicityValidator")
@RequestScoped
public class HouseNumberUnicityValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            FacesMessage message = new FacesMessage("Należy podać numer budynku.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }

        String houseNumber=((String) value);
        if (!houseNumber.matches("(^[0-9]{1,5}$)")) {

            FacesMessage message = new FacesMessage("Nieprawidłowy format numeru budynku");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
