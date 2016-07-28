
package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;


@Entity
@Audited
@Table(name = "roles")
@NamedNativeQueries({@NamedNativeQuery(name = "Role.removeRoleFromAllAccounts", query = "DELETE FROM account_roles WHERE role = ?")})
public class Role implements Serializable {
    private static final long serialVersionUID = -4767712582624098830L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotAudited
    private Long id;
    
    @Column
    private String name;
    
    @NotAudited
    @ManyToMany(mappedBy = "roles")
    private List<Account> accounts;
   
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Long getId(){
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role other = (Role) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return name;
	}
    
    
    
    

}
