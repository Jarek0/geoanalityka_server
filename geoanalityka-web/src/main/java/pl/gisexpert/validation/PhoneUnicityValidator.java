package pl.gisexpert.validation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "phoneUnicityValidator")
@RequestScoped
public class PhoneUnicityValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            FacesMessage message = new FacesMessage("Należy podać numer telefonu.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }

        String phonenumber=((String) value);
        if (!phonenumber.matches("^[0-9]{9,11}$")) {

            FacesMessage message = new FacesMessage("Numer telefonu może mieć od 9 do 11 cyfr");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
