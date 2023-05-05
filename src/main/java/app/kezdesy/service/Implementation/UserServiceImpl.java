package app.kezdesy.service.Implementation;


import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;
import app.kezdesy.repository.RoleRepository;
import app.kezdesy.repository.UserRepository;
import app.kezdesy.service.Interface.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, IUserService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;



    public boolean changePhoto(String email, String file) {
        User user = userRepository.findByEmail(email);
        user.setPicture(file);
        userRepository.save(user);
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database.");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public boolean register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) return false;

        user.getRoles().add(roleRepository.findById(1L).orElse(null));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean loginUser(User loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) return false;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) return false;
        return true;
    }

    public boolean deleteUser(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) return false;
        userRepository.delete(user);
        return true;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public Role saveRole(Role role) {
        return null;
    }

    @Override
    public void addRoleToUser(String email, String roleName) {

    }

    @Override
    public User getUser(String email) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

//    public Set<SimpleGrantedAuthority> getAuthority(User user) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        user.getRoles().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//        });
//        return authorities;
//    }

}
