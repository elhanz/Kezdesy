package app.kezdesy.entity;


import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



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

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToMany
    private Collection<Interest> interest = new ArrayList<>();


//    @ElementCollection(targetClass = Interests.class)
//    @CollectionTable(name = "room_interests", joinColumns = @JoinColumn(name = "room_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "interest")
//    private Set<Interests> interests;

}