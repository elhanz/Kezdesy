package app.kezdesy.model;

import app.kezdesy.entity.Interest;
import lombok.Data;

import java.util.Set;

@Data
public class ChangeInterestsRequest {
    private String email;
    private Set<Interest> interests;
}
