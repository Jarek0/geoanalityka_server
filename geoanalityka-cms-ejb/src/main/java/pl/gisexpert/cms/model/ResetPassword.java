
package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class ResetPassword implements Serializable {
    private static final long serialVersionUID = -5081745710024124741L;

    @Column(length = 64, name = "reset_pass_token")
    private String token;
   
    @Column(name = "reset_pass_token_exp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expDate;
}
