package app.kezdesy.model;

import lombok.Data;

@Data
public class EmailRoomIdRequest {
    private String email;
    private Long roomId;
}
