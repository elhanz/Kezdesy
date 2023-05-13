package app.kezdesy.controller;

import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.EmailRoomId;
import app.kezdesy.model.RoomEmailRequest;
import app.kezdesy.repository.RoomRepository;
import app.kezdesy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create")
    public ResponseEntity createRoom(@RequestBody RoomEmailRequest roomEmailRequest) {
        User curr_user = userRepository.findByEmail(roomEmailRequest.getEmail());

        Room room = new Room(roomEmailRequest.getCity(), roomEmailRequest.getHeader(), roomEmailRequest.getDescription(), roomEmailRequest.getMinAgeLimit(),
                roomEmailRequest.getMaxAgeLimit(), roomEmailRequest.getMaxMembers(), roomEmailRequest.getInterests(), roomEmailRequest.getEmail());
        if (isAgeLimitCorrect(room.getMinAgeLimit(), room.getMaxAgeLimit())) {
            if (room.getHeader().length() < 200 && room.getHeader() != null) {
                if (room.getDescription().length() < 600 && room.getDescription() != null) {
                    if (room.getMaxMembers() > 1 && room.getMaxMembers() < 21) {
                        if (!room.getInterests().isEmpty()) {
                            room.getUsers().add(curr_user);
                            roomRepository.save(room);
                            return new ResponseEntity("Room was created", HttpStatus.CREATED);
                        } else {
                            return ResponseEntity.badRequest().body("Room must contain at least 1 interest.");
                        }
                    } else {
                        return ResponseEntity.badRequest().body("Max members of room 1-20.");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Description text limit - 600 chars.");
                }
            } else {
                return ResponseEntity.badRequest().body("Header text limit - 200 chars.");
            }
        } else {
            return ResponseEntity.badRequest().body("Incorrect age limits.");
        }
    }

    @GetMapping("/find")
    public ResponseEntity<List<Room>> findRoom() {
        return new ResponseEntity<List<Room>>(roomRepository.findAll(), HttpStatus.CREATED);
    }

    @GetMapping("/getRoomById")
    public ResponseEntity<Room> findRoomById(@RequestParam Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            return ResponseEntity.ok(room.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/kickUser")
    public ResponseEntity kickUser(@RequestBody EmailRoomId emailRoomId) {
        Room room = roomRepository.findRoomById(emailRoomId.getRoomId());
        User user = userRepository.findByEmail(emailRoomId.getEmail());

        if (room != null && user!= null) {
            roomRepository.kickUser(room.getId(), user.getId());

            return new ResponseEntity( user.getFirst_name() +" " + user.getLast_name() + " was kicked", HttpStatus.CREATED);

        }
        return ResponseEntity.badRequest().body("Something is wrong");

    }

    public boolean isAgeLimitCorrect(int lower, int higher) {
        if (lower <= 11 || lower > higher) {
            return false;
        }
        if (higher <= 12) {
            return false;
        }
        if (higher - lower < 1) {
            return false;
        }
        if (lower > 100 || higher > 100) {
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
    public ResponseEntity joinRoom(@RequestBody EmailRoomId emailRoomId) {
        Room room = roomRepository.findById(emailRoomId.getRoomId()).orElse(null);

        if (!isUserInRoom(emailRoomId.getEmail(), room.getUsers())) {

            room.getUsers().add(userRepository.findByEmail(emailRoomId.getEmail()));

            roomRepository.save(room);

            return new ResponseEntity("User was added to room.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity("User is already in room", HttpStatus.BAD_REQUEST);
        }
    }
}
