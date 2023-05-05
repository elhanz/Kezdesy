package app.kezdesy.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "birth_Date")
    private Date birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "picture", columnDefinition = "text")
    private String picture;

    @Column(name = "gender")
    private boolean gender;


    @Column(name = "interests")
    private String interests;


    @ManyToOne
    private Location location;

    @ManyToMany
    private Collection<Room> rooms = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

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


