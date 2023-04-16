package app.kezdesy.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type")
    private String type;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;



}
