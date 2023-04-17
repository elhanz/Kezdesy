package app.kezdesy.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy="location")
    private Set<User> user;

    @OneToMany(mappedBy="location")
    private Set<Room> rooms;
}