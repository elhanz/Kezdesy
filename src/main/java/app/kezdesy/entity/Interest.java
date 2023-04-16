package app.kezdesy.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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


//    @ManyToMany(mappedBy = "interests")
//    private List<User> users;
//
//    @ManyToMany(mappedBy = "interest")
//    private List<Room> rooms;
}