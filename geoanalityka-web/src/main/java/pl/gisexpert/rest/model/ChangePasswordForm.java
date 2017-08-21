package pl.gisexpert.rest.model;

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class ChangePasswordForm {
    private String password;
    private String confirmPassword;
    private String resetPasswordToken;
}
