package app.kezdesy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "age")
    private int age;

    @Column(name = "city")
    private String city;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Type(type = "text")
    private String profilePic;

    @Column(name = "gender")
    private String gender;

    private boolean isEnabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();


    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Collection<Room> rooms = new ArrayList<>();

    @ElementCollection(targetClass = Interest.class)
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "interest")
    private Set<Interest> interests;


    public User(String first_name, String last_name, int age, String city, String email, String password, String profilePic, Set<Interest> interests) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.city = city;
        this.email = email;
        this.isEnabled = false;
        this.password = password;
        this.profilePic = profilePic;
        this.interests = interests;
    }
}
