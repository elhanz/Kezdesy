package app.kezdesy.model;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
