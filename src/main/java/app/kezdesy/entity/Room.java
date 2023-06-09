package app.kezdesy.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
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

    @Column(name = "owner")
    private String owner;

    @ManyToMany
    private Collection<User> users = new ArrayList<>();

    @ElementCollection(targetClass = Interest.class)
    @CollectionTable(name = "room_interests", joinColumns = @JoinColumn(name = "room_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "interest")
    private Set<Interest> interests;


    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Message> messages = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public Room(String city, String header, String description, int minAgeLimit, int maxAgeLimit, int maxMembers, Set<Interest> interests, String owner) {
        this.city = city;
        this.header = header;
        this.description = description;
        this.minAgeLimit = minAgeLimit;
        this.maxAgeLimit = maxAgeLimit;
        this.maxMembers = maxMembers;
        this.interests = interests;
        this.owner = owner;
    }


}
