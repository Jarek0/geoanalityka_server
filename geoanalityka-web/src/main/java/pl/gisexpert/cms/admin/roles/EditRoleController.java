package pl.gisexpert.cms.admin.roles;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Role;

@Named
@ViewScoped
public class EditRoleController implements Serializable {
    private static final long serialVersionUID = 3275428769208318741L;

    @Inject
    RoleRepository roleRepository;
    
    private Role role;


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public void save(){
        
        roleRepository.edit(role);
        
        
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Parametry grupy zostały zaktualizowane.");
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
  
    
}
