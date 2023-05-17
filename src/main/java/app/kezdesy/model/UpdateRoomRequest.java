package app.kezdesy.model;

import app.kezdesy.entity.Interest;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateRoomRequest {
    private Long id;
    private String city;
    private String header;
    private String description;
    private String owner;
    private int minAgeLimit;
    private int maxAgeLimit;
    private int maxMembers;
    private Set<Interest> interests;
}
