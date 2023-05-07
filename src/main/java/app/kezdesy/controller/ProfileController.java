package app.kezdesy.controller;

import app.kezdesy.entity.User;
import app.kezdesy.model.ChangePictureRequest;
import app.kezdesy.model.UpdatePasswordRequest;
import app.kezdesy.service.implementation.ProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

   @Autowired
    private ProfileServiceImpl profileService;


    @GetMapping("/getUser")
    public ResponseEntity<User>getUserByUsername(@RequestParam String email) {
        return ResponseEntity.ok().body(profileService.getUserByEmail(email));
    }

    @PostMapping("/updateProfile")
    public ResponseEntity updateProfile(@RequestBody User user){

        if (profileService.updateUser(user)) {
            return new ResponseEntity("User successfully updated", HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("User wasn't updated");
    }



    @PostMapping("/setPicture")
    public ResponseEntity changePhoto(@RequestBody ChangePictureRequest changePictureRequest) {

        if (profileService.changePhoto(changePictureRequest.getEmail(), changePictureRequest.getFile())) {
            return new ResponseEntity("Photo was changed", HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("error");
    }

    @PostMapping("/setPassword")
    public ResponseEntity updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {

        if (profileService.updateUserPassword(updatePasswordRequest.getEmail(), updatePasswordRequest.getPassword())) {
            return new ResponseEntity("Password was changed", HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("Password wasn't changed");
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity deleteProfile(@RequestParam String email){ //Сменил бади на парам
        if (profileService.deleteUserByEmail(email)) {
            return new ResponseEntity("User was deleted", HttpStatus.OK);
        }
        else return  ResponseEntity.badRequest().body("User wasn't deleted");

}

}
