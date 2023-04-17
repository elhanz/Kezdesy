package app.kezdesy.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "interest")
public class Interest {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @ManyToMany
    private Set<Room> room;
    }

