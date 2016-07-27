package pl.gisexpert.cms.roles;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Role;


@Named
@ViewScoped
public class DeleteRoleController implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(DeleteRoleController.class);
    private static final long serialVersionUID = 1516573149103434227L;

    private Long roleId;
    
    @Inject
    private RoleRepository roleRepository;

    public void delete() {
        Role roleToBeDeleted = roleRepository.find(roleId);
        roleRepository.removeRoleFromAllAccounts(roleToBeDeleted);
        roleRepository.remove(roleToBeDeleted);
        
        log.info("Role: " + roleToBeDeleted.getName() + " has been deleted.");
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

}
