
package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;


@Entity
@Audited
@Table(name = "roles")
@NamedNativeQueries({@NamedNativeQuery(name = "Role.removeRoleFromAllAccounts", query = "DELETE FROM account_roles WHERE role = ?")})

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode(of = "name")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class Role implements Serializable {
    private static final long serialVersionUID = -4767712582624098830L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotAudited
    private Long id;
    
    @Column
    private String name;
    
    @NotAudited
    @ManyToMany(mappedBy = "roles",cascade = CascadeType.REMOVE)
    private List<Account> accounts;

    public void addAccount(Account account){
        accounts.add(account);
    }
}
