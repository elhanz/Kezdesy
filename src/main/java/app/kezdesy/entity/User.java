package app.kezdesy.entity;


import org.hibernate.annotations.CreationTimestamp;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nickname")
    private String nickname;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "birth_Date")
    private Date birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "picture" , columnDefinition ="text")

    private String picture;

    @Column(name = "gender")
    private boolean gender;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_room",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "room_id")})
    private List<Room> rooms = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_interest",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "interest_id")})
    private List<Interest> interests = new ArrayList<>();

    @Column(name = "Role")
    private String Role;

//    @ElementCollection(targetClass = Interests.class)
//    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "interest")
//    private Set<Interests> interests;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}


