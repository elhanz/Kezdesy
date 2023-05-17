package app.kezdesy.controller;

import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.model.EmailRoomIdRequest;
import app.kezdesy.model.RoomMessageRequest;
import app.kezdesy.model.RoomRequest;
import app.kezdesy.model.UpdateRoomRequest;
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


    private final RoomValidation roomValidation = new RoomValidation();


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
    public ResponseEntity kickUser(@RequestBody EmailRoomIdRequest emailRoomIdRequest) {
        User user = userService.getUserByEmail(emailRoomIdRequest.getEmail());

        if (user != null || roomService.kickUser(user, emailRoomIdRequest.getRoomId())) {
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
    public ResponseEntity UpdateMessage(@RequestBody RoomMessageRequest roomMessageRequest) {

        if (roomMessageRequest.getContent() == null) {
            return ResponseEntity.badRequest().body("Fill the message");
        }
        if (roomService.updateMessage(roomMessageRequest)) {

            return ResponseEntity.ok("Message was updated");
        }
        return ResponseEntity.badRequest().body("Message not found");

    }

    @PostMapping("/updateRoom")
    public ResponseEntity updateRoom(@RequestBody UpdateRoomRequest updateRoomRequest) {

        if (!roomValidation.isAgeLimitCorrect(updateRoomRequest.getMinAgeLimit(), updateRoomRequest.getMaxAgeLimit())) {
            return ResponseEntity.badRequest().body("Incorrect age limits.");
        }
        if (!roomValidation.isHeaderCorrect(updateRoomRequest.getHeader().length())) {
            return ResponseEntity.badRequest().body("Header text limit is 3 - 200 chars.");
        }
        if (updateRoomRequest.getDescription() != null && !roomValidation.isDescriptionCorrect(updateRoomRequest.getDescription().length())) {
            return ResponseEntity.badRequest().body("Description text limit is 600 chars.");
        }
        if (!roomValidation.isMaxMembersCorrect(updateRoomRequest.getMaxMembers())) {
            return ResponseEntity.badRequest().body("Max members of the room is 1-20.");
        }
        if (updateRoomRequest.getInterests().isEmpty()) {
            return ResponseEntity.badRequest().body("Room must contain at least 1 interest.");
        }

        roomService.updateRoom(updateRoomRequest);

        return ResponseEntity.ok().body("Room was updated");
    }

    @GetMapping("/deleteRoom")
    public ResponseEntity deleteRoom(@RequestParam Long id) {

        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room was deleted");
    }

    @PostMapping("/join")
    public ResponseEntity joinRoom(@RequestBody EmailRoomIdRequest emailRoomIdRequest) {

        if (roomService.joinRoom(emailRoomIdRequest)) {
            return ResponseEntity.ok().body("User was added to room.");
        }
        return ResponseEntity.badRequest().body("User is already in room");


    }
}
