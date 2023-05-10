package app.kezdesy.controller;

import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.EmailRoomId;
import app.kezdesy.model.RoomEmailRequest;
import app.kezdesy.repository.RoomRepo;
import app.kezdesy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private UserRepo userRepo;


    @PostMapping("/create")
    public ResponseEntity createRoom(@RequestBody RoomEmailRequest roomEmailRequest){
        User curr_user = userRepo.findByEmail(roomEmailRequest.getEmail());
        Room room = new Room(roomEmailRequest.getCity(),roomEmailRequest.getHeader(),roomEmailRequest.getDescription(), roomEmailRequest.getMinAgeLimit(),
                roomEmailRequest.getMaxAgeLimit(),roomEmailRequest.getMaxMembers(),roomEmailRequest.getInterests(), roomEmailRequest.getOwner());
        if(isAgeLimitCorrect(room.getMinAgeLimit(), room.getMaxAgeLimit())){
            if(room.getHeader().length() < 200 && room.getHeader() != null){
                if(room.getDescription().length() < 600 && room.getDescription() != null){
                    if(room.getMaxMembers() > 1 && room.getMaxMembers() < 21){
                        if(!room.getInterests().isEmpty()){
                            room.getUsers().add(curr_user);
                            roomRepo.save(room);
                            return new ResponseEntity("Room was created", HttpStatus.CREATED);
                        }else{
                            return ResponseEntity.badRequest().body("Room must contain at least 1 interest.");
                        }
                    }else{
                        return ResponseEntity.badRequest().body("Max members of room 1-20.");
                    }
                }else{
                    return ResponseEntity.badRequest().body("Description text limit - 600 chars.");
                }
            }else{
                return ResponseEntity.badRequest().body("Header text limit - 200 chars.");
            }
        }else{
            return ResponseEntity.badRequest().body("Incorrect age limits.");
        }
    }

    @GetMapping("/find")
    public ResponseEntity<List<Room>> findRoom(){
        return new ResponseEntity<List<Room>>(roomRepo.findAll(), HttpStatus.CREATED);
    }

    public boolean isAgeLimitCorrect(int lower, int higher){
        if(lower <= 11 || lower > higher){
            return false;
        }
        if(higher <= 12){
            return false;
        }
        if(higher - lower < 1){
            return false;
        }
        if(lower > 100 || higher > 100){
            return false;
        }
        return true;
    }

    public boolean isUserInRoom(String email, Collection<User> users) {
        for (User user : users) {
            if (Objects.equals(user.getEmail(), email)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/join")
    public ResponseEntity joinRoom(@RequestBody EmailRoomId emailRoomId){
        Room room = roomRepo.findById(emailRoomId.getRoomId()).orElse(null);

        if (!isUserInRoom(emailRoomId.getEmail(), room.getUsers())){

            room.getUsers().add(userRepo.findByEmail(emailRoomId.getEmail()));

            roomRepo.save(room);

            return new ResponseEntity("User was added to room.", HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity("User is already in room", HttpStatus.BAD_REQUEST);
        }
    }
}
