package pl.gisexpert.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Role;

@Named
@RequestScoped
@FacesConverter(value = "roleConverter")
public class RoleConverter implements Converter {

    @Inject
    RoleRepository roleRepository;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        return roleRepository.findByName(value);

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        String name = ((Role)value).getName();
        return (name != null) ? name : null;

    }

}
