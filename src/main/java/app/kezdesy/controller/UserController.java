//package app.kezdesy.controller;
//
//import app.kezdesy.entity.User;
//import app.kezdesy.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/admin")
//public class UserController {
//
//
//    @Autowired
//    UserRepository userRepository;
//
//    @PostMapping("/save-user")
//    public ResponseEntity<User> saveUser(User user) {
//        return ResponseEntity.ok(userRepository.save(user));
//    }
//
//    @GetMapping("/get-users")
//    public ResponseEntity<List<User>> getUser() {
//        return ResponseEntity.ok(userRepository.findAll());
//    }
//
//
//}
