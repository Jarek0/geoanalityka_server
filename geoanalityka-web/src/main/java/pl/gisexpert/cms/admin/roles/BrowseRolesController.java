package pl.gisexpert.cms.admin.roles;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Role;

@Named
@RequestScoped
public class BrowseRolesController {

    @Inject
    RoleRepository roleRepository;

    private List<Role> roles;
    
    private Role newRole;

    public BrowseRolesController(){
        newRole = new Role();
    }
    
    @PostConstruct
    public void setupRoles() {
        roles = roleRepository.findAll();
    }
    
    public void addRole(){
        if (newRole.getName() != null){
            roleRepository.create(newRole);
            roles.add(newRole);
            newRole = new Role();
        }
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Role getNewRole() {
        return newRole;
    }

    public void setNewRole(Role newRole) {
        this.newRole = newRole;
    }    
}
