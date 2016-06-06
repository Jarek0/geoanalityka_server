package pl.gisexpert.security.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

public class BearerTokenAuthenticatingFilter extends AuthenticatingFilter {

    
    @Override
    protected AuthenticationToken createToken(ServletRequest sr, ServletResponse sr1) throws Exception {
        String token = WebUtils.getCleanParam(sr, "token");
        
        AuthenticationToken authToken = new BearerAuthenticationToken(token);
        return authToken;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest sr, ServletResponse sr1) throws Exception {
        return executeLogin(sr, sr1);
    }
   
}
