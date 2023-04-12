package app.kezdesy.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(name = "is_active")
    private boolean isActive;


}