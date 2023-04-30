package app.kezdesy.entity;


import app.kezdesy.constant.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @JsonFormat(pattern="dd/MM/yyyy")
    @Column(name = "birth_Date")
    private Date birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "picture", columnDefinition = "text")
    private String picture;

    @Column(name = "gender")
    private boolean gender;


    @Column(name = "interests")
    private String interests;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Message> messages;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany
    @JoinTable(name = "room_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "room_id")})
    private List<Room> room;


    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_interest",
//            joinColumns = {@JoinColumn(name = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "interest_id")})
//    private List<Interest> userInterests = new ArrayList<>();


//    @ElementCollection(targetClass = Interests.class)
//    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "interest")
//    private Set<Interests> interests;


}


