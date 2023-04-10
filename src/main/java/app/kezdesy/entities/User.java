package app.kezdesy.entities;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nickname")
    private String nickname;
    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "birth_Date")
    private Date birth_date;

    @Column(name = "city")
    private String city;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Type(type = "text")
    private String picture;

    @Column(name = "gender")
    private boolean gender;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public User(String nickname, String first_name, String last_name, Date birth_date, String city, String email, String password, String picture, boolean gender, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.city = city;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.gender = gender;
        this.createdAt = createdAt;
    }

    public User() {

    }

//    @ManyToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private Collection<Room> rooms = new ArrayList<>();


}


