package app.kezdesy.controller;

import app.kezdesy.entity.Chat;
import app.kezdesy.entity.ChatMessage;
import app.kezdesy.entity.User;
import app.kezdesy.repository.ChatMessageRepo;
import app.kezdesy.repository.ChatRepo;
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
    private ChatRepo chatRepo;

    @Autowired
    private ChatMessageRepo chatMessageRepo;

    public ChatController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
    public ResponseEntity<Collection<Chat>> getMyChats(String userEmail){
        return new ResponseEntity<>(userRepo.findByEmail(userEmail).getChats(), HttpStatus.CREATED);
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
    public ResponseEntity<Chat> getChatById(@RequestParam Long chatID) {
        return ResponseEntity.ok().body(chatRepo.findById(chatID).orElse(null));
    }

    @GetMapping("/getUserForChatById")
    public ResponseEntity<User> getUserById(@RequestParam Long userID) {
        return ResponseEntity.ok().body(userRepo.findById(userID).orElse(null));
    }

    @MessageMapping("/chat.sendMessage/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable Long chatId) {
        Chat chat = chatRepo.findById(chatId).orElse(null);
        chat.getMessages().add(chatMessage);
        chatMessageRepo.save(chatMessage);
        chatRepo.save(chat);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor,
                               @DestinationVariable Long chatId) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessage message(@DestinationVariable Long chatId, @Payload ChatMessage chatMessage){
        return chatMessage;
    }


}
