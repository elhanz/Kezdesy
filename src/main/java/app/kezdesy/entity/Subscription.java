package app.kezdesy.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;


@Entity
@Data
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy="subscription")
    private Set<User> user;

}