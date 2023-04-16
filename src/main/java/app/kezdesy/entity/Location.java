package app.kezdesy.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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




}