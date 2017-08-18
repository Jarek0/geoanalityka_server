package pl.gisexpert.rest.model;

public class ChangePasswordForm {
    private String password;
    private String confirmPassword;
    private String resetPasswordToken;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String changePasswordToken) {
        this.resetPasswordToken = changePasswordToken;
    }
}
