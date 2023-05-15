package app.kezdesy.model;

import app.kezdesy.entity.Interest;
import lombok.Data;

import java.util.Set;

@Data
public class RoomRequest {
    private String email;
    private String city;
    private String header;
    private String description;
    private int minAgeLimit;
    private int maxAgeLimit;
    private int maxMembers;
    private Set<Interest> interests;
}
