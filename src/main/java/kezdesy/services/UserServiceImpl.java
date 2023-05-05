package kezdesy.services;

import kezdesy.entities.User;
import kezdesy.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public boolean changePhoto(String email, String file) {
        User user = userRepo.findByEmail(email);
        user.setProfilePic(file);
        userRepo.save(user);
        return true;
    }

    public User findById(Long id) {
        User user = userRepo.findById(id).orElse(null);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User not found in the database.");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
