package pl.gisexpert.security.shiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.sql.DataSource;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BearerAuthenticationRealm extends AuthorizingRealm {

    protected DataSource dataSource;

    protected String accountInfoQuery = "SELECT accounts.id, accounts.username, access_tokens.expires\n"
            + "FROM accounts\n"
            + "INNER JOIN account_tokens ON account_tokens.account_id = accounts.id\n"
            + "INNER JOIN access_tokens ON access_tokens.id = account_tokens.token_id\n"
            + "WHERE access_tokens.token = ?";
    protected String userRolesQuery = "SELECT role FROM account_roles WHERE username = ?";

    private class AccountInfo {
        public Long id;
        public String username;
        public Date expires;
    }

    Logger log = LoggerFactory.getLogger(BearerAuthenticationRealm.class);

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BearerAuthenticationRealm() {
        //this makes the supports(...) method return true only if the token is an instanceof BearerAuthenticationToken:
        setAuthenticationTokenClass(BearerAuthenticationToken.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {

        BearerAuthenticationToken token = (BearerAuthenticationToken) authToken;
        
        if (token.getAccessToken() == null) {
            throw new AccountException("Null accessToken is not allowed by this realm.");
        }

        AuthenticationInfo info;
        Connection conn = null;
        AccountInfo accountInfo = null;
        
        try {
            conn = dataSource.getConnection();
            accountInfo = getAccountInfo(conn, token);
            
            if (new Date().after(accountInfo.expires)) {
                throw new AuthenticationException("Invalid token.");
            }
           
            info = new SimpleAuthenticationInfo(accountInfo.username, token.accessToken, getName());
            return info;
            
        } catch (SQLException ex) {
            final String message = "There was a SQL error while authenticating token [" + token.getPrincipal() + "]";
            log.error(message);
        } finally {
            JdbcUtils.closeConnection(conn);
        }
        
        return null;
    }

    private AccountInfo getAccountInfo(Connection conn, BearerAuthenticationToken bearerToken) {

        String token = bearerToken.getAccessToken();
        PreparedStatement ps = null;
        ResultSet rs = null;

        AccountInfo accountInfo = new AccountInfo();

        try {
            ps = conn.prepareStatement(accountInfoQuery);
            ps.setString(1, token);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                accountInfo.id = rs.getLong("id");
                accountInfo.username = rs.getString("username");
                accountInfo.expires = rs.getTimestamp("expires");
            }
            
        } catch (Exception e) {
        	throw new AuthorizationException("Unable to retrieve account info.");
        }
        finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }

        return accountInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);
        
        Connection conn = null;
        Set<String> roleNames = null;
        
        try {
            conn = dataSource.getConnection();
            roleNames = getRoleNamesForUser(conn, username);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(BearerAuthenticationRealm.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JdbcUtils.closeConnection(conn);
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);

        return info;

    }
    
    protected Set<String> getRoleNamesForUser(Connection conn, String username) throws SQLException {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        Set<String> roleNames = new LinkedHashSet<>();
        
        try {
            ps = conn.prepareStatement(userRolesQuery);
            ps.setString(1, username);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                String roleName = rs.getString(1);
                if (roleName != null){
                    roleNames.add(roleName);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Null role name found while retrieving role names for user [" + username + "]");
                    }
                }
            }
            
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
        
        return roleNames;
        
    }
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof BearerAuthenticationToken;
    }

}
