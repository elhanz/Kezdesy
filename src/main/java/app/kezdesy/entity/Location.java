package app.kezdesy.entity;

import javax.persistence.*;
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


}