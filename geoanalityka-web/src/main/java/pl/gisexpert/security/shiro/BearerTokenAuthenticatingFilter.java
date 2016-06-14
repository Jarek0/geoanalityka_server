package pl.gisexpert.security.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

public class BearerTokenAuthenticatingFilter extends AuthenticatingFilter {
	
	@Override
	protected AuthenticationToken createToken(ServletRequest sr, ServletResponse sr1) throws Exception {		
		
		HttpServletRequest request = (HttpServletRequest) sr;
		String token = request.getHeader("Access-Token");
		
		AuthenticationToken authToken = new BearerAuthenticationToken(token);
		return authToken;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		if (executeLogin(request, response)) {
			return true;
		}

		HttpServletResponse httpResponse;

		try {
			httpResponse = WebUtils.toHttp(response);

		} catch (ClassCastException ex) {
			// Not a HTTP Servlet operation
			return super.onAccessDenied(request, response, null);
		}

		httpResponse.sendError(403);

		return false;

	}

}
