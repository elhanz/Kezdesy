package app.kezdesy.model;

import lombok.Data;

@Data
public class ChangePictureRequest {
    private String email;
    private String file;
}
