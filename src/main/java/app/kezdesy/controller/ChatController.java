package app.kezdesy.controller;

import app.kezdesy.entity.Message;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.repository.MessageRepository;
import app.kezdesy.repository.RoomRepository;
import app.kezdesy.repository.UserRepository;
import app.kezdesy.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.AbstractDestinationResolvingMessagingTemplate;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    private final UserServiceImpl userService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MessageRepository chatMessageRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ChatController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
    public ResponseEntity<Collection<Room>> getMyChats(String userEmail){
        return new ResponseEntity<>(userRepository.findByEmail(userEmail).getRooms(), HttpStatus.CREATED);
    }

    @GetMapping("/getUserForChat")
    public ResponseEntity<User> getUserByUsernameForChat(@RequestParam String email) {
        return ResponseEntity.ok().body(userRepository.findByEmail(email));
    }

    @GetMapping("/getRepo")
    public ResponseEntity<UserServiceImpl> getUserRepo() {
        return ResponseEntity.ok().body(userService);
    }

    @GetMapping("/getChat")
    public ResponseEntity<Room> getChatById(@RequestParam Long chatID) {
        return ResponseEntity.ok().body(roomRepository.findById(chatID).orElse(null));
    }

    @GetMapping("/getUserForChatById")
    public ResponseEntity<User> getUserById(@RequestParam Long userID) {
        return ResponseEntity.ok().body(userRepository.findById(userID).orElse(null));
    }

    @MessageMapping("/chat.sendMessage/{chatId}")
    @SendTo("/topic/{chatId}")
    public Message sendMessage(@Payload Message message, @DestinationVariable Long chatId) {
        Room room = roomRepository.findById(chatId).orElse(null);
        room.getMessages().add(message);
        chatMessageRepo.save(message);
        roomRepository.save(room);
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

    @MessageMapping("/chat.deleteMessage/{chatId}")
    @SendTo("/topic/{chatId}/messageDeleted")
    public Long deleteMessage(@DestinationVariable Long chatId, @Payload Long messageId) {
        Room room = roomRepository.findById(chatId).orElse(null);
        if (room != null) {
            // Find the message in the room's message list
            Optional<Message> optionalMessage = room.getMessages().stream()
                    .filter(message -> message.getId().equals(messageId))
                    .findFirst();
            if (optionalMessage.isPresent()) {
                Message message = optionalMessage.get();
                // Update the message content
                message.setContent("Message was deleted");
                // Save the modified message
                chatMessageRepo.save(message);
                // Return the deleted message ID
                return messageId;
            }
        }
        return null;
    }
}
