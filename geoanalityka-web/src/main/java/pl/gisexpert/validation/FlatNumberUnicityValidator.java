package pl.gisexpert.validation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean(name = "flatNumberUnicityValidator")
@RequestScoped
public class FlatNumberUnicityValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        String flatnumber=((String) value);

        if (flatnumber.isEmpty()) {
            return;
        }

        if (!flatnumber.matches("(^[0-9]{0,5}$)")) {

            FacesMessage message = new FacesMessage("Nieprawidłowy format numeru mieszkania");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
