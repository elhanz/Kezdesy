package app.kezdesy.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue
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

    @Column(name = "picture", columnDefinition = "text")

    private String picture;

    @Column(name = "gender")
    private boolean gender;


    @Column(name = "interests")
    private String interests ;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user")
    private Set<Message> messages;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "room_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "room_id")})
    private List<Room> rooms = new ArrayList<>();


    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_interest",
//            joinColumns = {@JoinColumn(name = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "interest_id")})
//    private List<Interest> userInterests = new ArrayList<>();




//    @ElementCollection(targetClass = Interests.class)
//    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "interest")
//    private Set<Interests> interests;


}


