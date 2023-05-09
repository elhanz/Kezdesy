package app.kezdesy.controller;

import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.ChangeInterestsRequest;
import app.kezdesy.model.ChangePictureRequest;
import app.kezdesy.model.DeleteProfileRequest;
import app.kezdesy.model.UpdatePasswordRequest;
import app.kezdesy.service.implementation.ProfileServiceImpl;
import app.kezdesy.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ProfileController {

    @Autowired
    private ProfileServiceImpl profileService;

    private UserValidation userValidation = new UserValidation();

    @GetMapping("/getUser")
    public ResponseEntity<User> getUserByUsername(@RequestParam String email) {
        return ResponseEntity.ok().body(profileService.getUserByEmail(email));
    }

    @PostMapping("/updateProfile")
    public ResponseEntity updateProfile(@RequestBody User user) {

        if (!userValidation.isNameValid(user.getFirst_name())) {
            return ResponseEntity.badRequest().body("Invalid name.");
        }
        if (!userValidation.isNameValid(user.getLast_name())) {
            return ResponseEntity.badRequest().body("Invalid surname.");
        }
        if (!userValidation.isAgeValid(user.getAge())) {
            return ResponseEntity.badRequest().body("Invalid age.");
        }
        if (!userValidation.isGenderValid(user.getGender())) {
            return ResponseEntity.badRequest().body("Invalid gender.");
        }
        if (!userValidation.isPasswordValid(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password must contain 8 or more symbols.");
        }
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

    @PostMapping("/setInterests")
    public ResponseEntity changeInterests(@RequestBody ChangeInterestsRequest changeInterestsRequest) {

        if (profileService.setInterests(changeInterestsRequest.getEmail(), changeInterestsRequest.getInterests())) {
            return new ResponseEntity("Interests were set", HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("Error Interests ");
    }


    @PostMapping("/setPassword")
    public ResponseEntity updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {

        if (profileService.updateUserPassword(updatePasswordRequest.getEmail(), updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword())) {
            return new ResponseEntity("Password was changed", HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("Password wasn't changed");
    }

    @PostMapping("/deleteUser")
    public ResponseEntity deleteProfile(@RequestBody DeleteProfileRequest deleteProfileRequest) {

        if (profileService.deleteUserByEmail(deleteProfileRequest.getEmail())) {
            return new ResponseEntity("User was deleted", HttpStatus.OK);
        } else return ResponseEntity.badRequest().body("User wasn't deleted");

    }

    @GetMapping("/myRooms")
    public ResponseEntity<List<Room>> myRooms(@RequestParam String email) {
        return ResponseEntity.ok().body(profileService.getUserRooms(email));
    }

}
