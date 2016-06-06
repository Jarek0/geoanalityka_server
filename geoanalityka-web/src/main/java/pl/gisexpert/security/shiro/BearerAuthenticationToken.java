
package pl.gisexpert.security.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BearerAuthenticationToken implements AuthenticationToken {
    private static final long serialVersionUID = -209983244840001440L;
    Logger log = LoggerFactory.getLogger(BearerAuthenticationToken.class);
   
    
    String accessToken;    
    
    public BearerAuthenticationToken(String accessToken){
        this.accessToken = accessToken;
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
