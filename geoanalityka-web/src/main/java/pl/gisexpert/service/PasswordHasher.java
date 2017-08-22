package pl.gisexpert.service;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;

import javax.faces.bean.ApplicationScoped;
import java.io.Serializable;

@ApplicationScoped
public class PasswordHasher implements Serializable{
    private static final long serialVersionUID = 1497269795945890070L;

    public String hashPassword(String password) {
        DefaultPasswordService passwordService = new DefaultPasswordService();
        DefaultHashService dhs = new DefaultHashService();
        dhs.setHashIterations(5);
        dhs.setHashAlgorithmName("SHA-256");
        dhs.setGeneratePublicSalt(true);
        dhs.setRandomNumberGenerator(new SecureRandomNumberGenerator());
        passwordService.setHashService(dhs);
        return passwordService.encryptPassword(password);
    }
}
