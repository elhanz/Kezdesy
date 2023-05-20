package app.kezdesy.controller;

import app.kezdesy.entity.Room;
import app.kezdesy.model.RoomRequest;
import app.kezdesy.service.implementation.RoomServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/room/find")
public class RoomFindController {


    private final RoomServiceImpl roomService;

    @GetMapping("/getAllRooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<List<Room>>(roomService.getAllRooms(), HttpStatus.CREATED);
    }

    @PostMapping("/findRoom")
    public ResponseEntity<List<Room>> findRooms(@RequestBody RoomRequest roomRequest) {

        return new ResponseEntity<List<Room>>(roomService.findRoom(roomRequest), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<Room>> recommendRooms(@RequestParam String email) {
        return new ResponseEntity<List<Room>>(roomService.recommendRooms(email), HttpStatus.OK);
    }


}
