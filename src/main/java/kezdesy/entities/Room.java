package kezdesy.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "room")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "header")
    private String header;

    @Column(name = "description")
    private String description;

    @Column(name = "minAge")
    private int minAgeLimit;

    @Column(name = "maxAge")
    private int maxAgeLimit;

    @Column(name = "maxMembers")
    private int maxMembers;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<User> members = new ArrayList<>();

    @ElementCollection(targetClass = Interest.class)
    @CollectionTable(name = "room_interests", joinColumns = @JoinColumn(name = "room_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "interest")
    private Set<Interest> interests;

    public Room(String city, String header, String description, int minAgeLimit, int maxAgeLimit, int maxMembers, Set<Interest> skillSet) {
        this.city = city;
        this.header = header;
        this.description = description;
        this.minAgeLimit = minAgeLimit;
        this.maxAgeLimit = maxAgeLimit;
        this.maxMembers = maxMembers;
        this.interests = skillSet;
    }

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(int minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public int getMaxAgeLimit() {
        return maxAgeLimit;
    }

    public void setMaxAgeLimit(int maxAgeLimit) {
        this.maxAgeLimit = maxAgeLimit;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Collection<User> getMembers() {
        return members;
    }

    public void setMembers(Collection<User> members) {
        this.members = members;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }
}
