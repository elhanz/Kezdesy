package app.kezdesy.controller;

import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.repository.ChatMessageRepo;
import app.kezdesy.repository.RoomRepo;
import app.kezdesy.repository.UserRepo;
import app.kezdesy.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private UserRepo userRepo;

    private final UserServiceImpl userService;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private ChatMessageRepo chatMessageRepo;

    public ChatController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
    public ResponseEntity<Collection<Room>> getMyChats(String userEmail){
        return new ResponseEntity<>(userRepo.findByEmail(userEmail).getRooms(), HttpStatus.CREATED);
    }

    @GetMapping("/getUserForChat")
    public ResponseEntity<User> getUserByUsernameForChat(@RequestParam String email) {
        return ResponseEntity.ok().body(userRepo.findByEmail(email));
    }

    @GetMapping("/getRepo")
    public ResponseEntity<UserServiceImpl> getUserRepo() {
        return ResponseEntity.ok().body(userService);
    }

    @GetMapping("/getChat")
    public ResponseEntity<Room> getChatById(@RequestParam Long chatID) {
        return ResponseEntity.ok().body(roomRepo.findById(chatID).orElse(null));
    }

    @GetMapping("/getUserForChatById")
    public ResponseEntity<User> getUserById(@RequestParam Long userID) {
        return ResponseEntity.ok().body(userRepo.findById(userID).orElse(null));
    }

    @MessageMapping("/chat.sendMessage/{chatId}")
    @SendTo("/topic/{chatId}")
    public Message sendMessage(@Payload Message message, @DestinationVariable Long chatId) {
        Room room = roomRepo.findById(chatId).orElse(null);
        room.getMessages().add(message);
        chatMessageRepo.save(message);
        roomRepo.save(room);
        return message;
    }

    @MessageMapping("/chat.addUser/{chatId}")
    @SendTo("/topic/{chatId}")
    public Message addUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor,
                           @DestinationVariable Long chatId) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/{chatId}")
    public Message message(@DestinationVariable Long chatId, @Payload Message message){
        return message;
    }


}
