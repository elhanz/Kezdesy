package app.kezdesy.entity;



import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

@Table(name = "interest")
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;




}