//package app.kezdesy.service;
//
//import app.kezdesy.entity.User;
//import app.kezdesy.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Service
//public class UserService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found in the database.");
//        }
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
////        user.getRoles().forEach(role -> {authorities.add(new SimpleGrantedAuthority(role.getName()));});
////        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
//        return null;
//    }
//}
//
