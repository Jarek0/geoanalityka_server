
package pl.gisexpert.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AccountConfirmation implements Serializable {
    private static final long serialVersionUID = -5081745710024124741L;

    @Column(length = 36, name = "confirmation_token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
