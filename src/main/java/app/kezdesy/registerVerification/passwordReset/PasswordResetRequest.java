package app.kezdesy.registerVerification.passwordReset;

import lombok.Data;


@Data
public class PasswordResetRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
