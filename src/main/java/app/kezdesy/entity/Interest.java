package app.kezdesy.entity;

import javax.persistence.*;
import java.util.List;
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

    @Column(name = "icon", columnDefinition = "text")
    private String icon;

    @ManyToMany(mappedBy = "interest")
    private List<Room> room;
    }

