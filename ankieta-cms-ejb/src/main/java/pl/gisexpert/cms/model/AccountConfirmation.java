
package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class AccountConfirmation implements Serializable {
    private static final long serialVersionUID = -5081745710024124741L;

    @Column(length = 36, name = "confirmation_token")
    private String token;

    public AccountConfirmation(UUID confirmationCode){
        this.token = confirmationCode.toString();
    }
}
