package app.kezdesy.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
@Data
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "header")
    private String header;

    @Column(name = "description")
    private String description;

    @Column(name = "minAge")
    private int minAgeLimit;

    @Column(name = "maxAge")
    private int maxAgeLimit;

    @Column(name = "maxMembers")
    private int maxMembers;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<User> members = new ArrayList<>();

    @ElementCollection(targetClass = Interest.class)
    @CollectionTable(name = "room_interests", joinColumns = @JoinColumn(name = "room_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "interest")
    private Set<Interest> interests;

    public Room(String city, String header, String description, int minAgeLimit, int maxAgeLimit, int maxMembers, Set<Interest> skillSet) {
        this.city = city;
        this.header = header;
        this.description = description;
        this.minAgeLimit = minAgeLimit;
        this.maxAgeLimit = maxAgeLimit;
        this.maxMembers = maxMembers;
        this.interests = skillSet;
    }


}
