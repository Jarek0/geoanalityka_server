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

import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Role;

@ManagedBean(name = "roleUnicityValidator")
@RequestScoped
public class RoleUnicityValidator implements Validator {
    
    @Inject
    private RoleRepository roleFacade;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value==null) {
            FacesMessage message = new FacesMessage("Nazwa grupy nie może być pusta.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }
        
        Role role = roleFacade.findByName((String)value);
        if (role != null) {
            FacesMessage message = new FacesMessage("Grupa o podanej nazwie już istnieje.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            
            if (component.getAttributes().get("roleId") != null) {
                Long roleId = (Long) component.getAttributes().get("roleId");
                if (!Objects.equals(roleId, role.getId())) {
                    throw new ValidatorException(message);
                }
            }
            else {
                throw new ValidatorException(message);
            }
       }
    }
}
