package app.kezdesy.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue
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


    @OneToMany(mappedBy = "room")
    private Set<Message> messages;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany
    private List<User> user = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

//    @ManyToMany
//    @JoinTable(
//            name = "room_interest",
//            joinColumns = @JoinColumn(name = "room_id"),
//            inverseJoinColumns = @JoinColumn(name = "interest_id"))
//    private List<Interest> roomInterests = new ArrayList<>();

//    @ElementCollection(targetClass = Interests.class)
//    @CollectionTable(name = "room_interests", joinColumns = @JoinColumn(name = "room_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "interest")
//    private Set<Interests> interests;

}