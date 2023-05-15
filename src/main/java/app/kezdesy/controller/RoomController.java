package app.kezdesy.controller;

import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.EmailRoomId;
import app.kezdesy.model.RoomMessage;
import app.kezdesy.model.RoomRequest;
import app.kezdesy.repository.RoomRepository;
import app.kezdesy.service.implementation.RoomServiceImpl;
import app.kezdesy.service.implementation.UserServiceImpl;
import app.kezdesy.validation.RoomValidation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomRepository roomRepository;

    private final RoomServiceImpl roomService;
    private final UserServiceImpl userService;


    private final RoomValidation roomValidation;


    @PostMapping("/create")
    public ResponseEntity createRoom(@RequestBody RoomRequest roomRequest) {


        if (!roomValidation.isAgeLimitCorrect(roomRequest.getMinAgeLimit(), roomRequest.getMaxAgeLimit())) {
            return ResponseEntity.badRequest().body("Incorrect age limits.");
        }
        if (!roomValidation.isHeaderCorrect(roomRequest.getHeader().length())) {
            return ResponseEntity.badRequest().body("Header text limit is 3 - 200 chars.");
        }
        if (!roomValidation.isDescriptionCorrect(roomRequest.getDescription().length())) {
            return ResponseEntity.badRequest().body("Description text limit is 600 chars.");
        }
        if (!roomValidation.isMaxMembersCorrect(roomRequest.getMaxMembers())) {
            return ResponseEntity.badRequest().body("Max members of the room is 1-20.");
        }
        if (roomRequest.getInterests().isEmpty()) {
            return ResponseEntity.badRequest().body("Room must contain at least 1 interest.");
        }

        Room room = new Room(roomRequest.getCity(), roomRequest.getHeader(), roomRequest.getDescription(), roomRequest.getMinAgeLimit(),
                roomRequest.getMaxAgeLimit(), roomRequest.getMaxMembers(), roomRequest.getInterests(), roomRequest.getEmail());
        User user = userService.getUserByEmail(roomRequest.getEmail());
        room.getUsers().add(user);
        Message messageJoined = roomService.createMessage(user);
        room.getMessages().add(messageJoined);
        roomService.createRoom(room);


        return ResponseEntity.ok().body("Room was created");

    }

    @GetMapping("/find")
    public ResponseEntity<List<Room>> findRoom() {
        return new ResponseEntity<List<Room>>(roomService.getAllRooms(), HttpStatus.OK);
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
        User user = userService.getUserByEmail(emailRoomId.getEmail());

        if (user != null || roomService.kickUser(user, emailRoomId.getRoomId())) {
            return ResponseEntity.ok(user.getFirst_name() + " " + user.getLast_name() + " left!");

        }
        return ResponseEntity.badRequest().body("Something is wrong");

    }

    @GetMapping("/deleteMessage")
    public ResponseEntity deleteMessage(@RequestParam Long id) {

        Message message = roomService.getMessageById(id);
        if (message != null) {
            roomService.deleteMessage(id);

            return ResponseEntity.ok("Message was deleted");
        } else return ResponseEntity.badRequest().body("Message not found");

    }


    @PostMapping("/updateMessage")
    public ResponseEntity UpdateMessage(@RequestBody RoomMessage roomMessage) {

        if (roomMessage.getContent() == null) {
            return ResponseEntity.badRequest().body("Fill the message");
        }
        if (roomService.updateMessage(roomMessage)) {

            return ResponseEntity.ok("Message was updated");
        }
        return ResponseEntity.badRequest().body("Message not found");

    }


    @PostMapping("/join")
    public ResponseEntity joinRoom(@RequestBody EmailRoomId emailRoomId) {

        if (roomService.joinRoom(emailRoomId)) {
            return ResponseEntity.ok().body("User was added to room.");
        }
        return ResponseEntity.badRequest().body("User is already in room");


    }
}
