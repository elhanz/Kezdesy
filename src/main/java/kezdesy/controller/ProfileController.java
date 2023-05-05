package kezdesy.controller;

import kezdesy.entities.User;
import kezdesy.model.DeleteProfileRequest;
import kezdesy.model.ProfilePicEmailRequest;
import kezdesy.repositories.UserRepo;
import kezdesy.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MainController mainController;

    private final UserServiceImpl userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ProfileController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public ResponseEntity<User>getUserByUsername(@RequestParam String email) {
        return ResponseEntity.ok().body(userRepo.findByEmail(email));
    }

    @PostMapping("/update")
    public ResponseEntity updateProfile(@RequestBody User user){
        int x = mainController.userIsValid(user);
        User user2 = userRepo.findByEmail(user.getEmail());
        if(x == 0 || x == 1){
            user2.setFirst_name(user.getFirst_name());
            user2.setLast_name(user.getLast_name());
            user2.setAge(user.getAge());
            user2.setGender(user.getGender());
            user2.setCity(user.getCity());

            userRepo.save(user2);

        }else{
            return ResponseEntity.badRequest().body("Some field is incorrect.");
        }
        return ResponseEntity.ok().body("User updated");
    }

    @PostMapping("/setPicture")
    public ResponseEntity changePhoto(@RequestBody ProfilePicEmailRequest profilePicEmailRequest) {
        String file = profilePicEmailRequest.getFile().replace("{\"file\":\"", "");
        file = file.replace("\"}","");
        if (userService.changePhoto(profilePicEmailRequest.getEmail(), file)) {
            return new ResponseEntity("photo changed", HttpStatus.ACCEPTED);
        }
        return ResponseEntity.badRequest().body("error");
    }

    @PostMapping("/deleteUser")
    public ResponseEntity deleteProfile(@RequestBody DeleteProfileRequest deleteProfileRequest){
        User user = userRepo.findByEmail(deleteProfileRequest.getEmail());
        userRepo.deleteById(user.getId());
        return ResponseEntity.ok().body("User was deleted.");
    }
}


