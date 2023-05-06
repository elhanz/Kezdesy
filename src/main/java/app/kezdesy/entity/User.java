package app.kezdesy.entity;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Chat> chats = new ArrayList<>();

    public User(String first_name, String last_name, int age, String city, String email, String password, String profilePic) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.city = city;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
    }



}