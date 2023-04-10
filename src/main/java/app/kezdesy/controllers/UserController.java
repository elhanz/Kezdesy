package app.kezdesy.controllers;

import app.kezdesy.entities.User;
import app.kezdesy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/admin/user")
    public ResponseEntity<User> saveUser(User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/user/user")
    public ResponseEntity<List> getUser() {
        return ResponseEntity.ok(userRepository.findAll());
    }


}
