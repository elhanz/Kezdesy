package app.kezdesy.controller;

import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.*;
import app.kezdesy.repository.RoomRepo;
import app.kezdesy.service.implementation.ProfileServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class ProfileController {

   @Autowired
    private ProfileServiceImpl profileService;
    @Autowired
    private RoomRepo roomRepo;


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
    public ResponseEntity deleteProfile(@RequestBody DeleteProfileRequest deleteProfileRequest){

        if (profileService.deleteUserByEmail(deleteProfileRequest.getEmail())) {
            return new ResponseEntity("User was deleted", HttpStatus.OK);
        }
        else return  ResponseEntity.badRequest().body("User wasn't deleted");

    }


    @PostMapping("/myRooms")
    public ResponseEntity<List<Room>> myRooms(@RequestParam String email){

        User user = profileService.getUserByEmail(email);
        List<Room> rooms = roomRepo.myRooms(user.getId());
        return new ResponseEntity<List<Room>>(rooms, HttpStatus.CREATED);
    }

}
