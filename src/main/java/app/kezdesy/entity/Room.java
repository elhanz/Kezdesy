package app.kezdesy.entity;


import lombok.Data;

import javax.persistence.*;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture")
    private String picture;

    @Column(name = "max_members")
    private int maxMembers;

    @Column(name = "age_restriction")
    private String ageRestriction;

    @Column(name = "members_quantity")
    private int membersQuantity;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "room_interest",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "interest_id")})
    private List<Interest> interests = new ArrayList<>();

//    @ElementCollection(targetClass = Interests.class)
//    @CollectionTable(name = "room_interests", joinColumns = @JoinColumn(name = "room_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "interest")
//    private Set<Interests> interests;

}