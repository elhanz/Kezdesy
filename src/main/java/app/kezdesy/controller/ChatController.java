//package app.kezdesy.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.*;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Collection;
//
//@Controller
//@RequestMapping("/chats")
//public class ChatController {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    private final UserServiceImpl userService;
//
//    @Autowired
//    private ChatRepo chatRepo;
//
//    @Autowired
//    private MessageRepo messageRepo;
//
//    public ChatController(UserServiceImpl userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/my")
//    public ResponseEntity<Collection<Chat>> getMyChats(String userEmail){
//        return new ResponseEntity<>(userRepo.findByEmail(userEmail).getChats(), HttpStatus.CREATED);
//    }
//
//    @GetMapping("/getUserForChat")
//    public ResponseEntity<User> getUserByUsernameForChat(@RequestParam String email) {
//        return ResponseEntity.ok().body(userRepo.findByEmail(email));
//    }
//
//    @GetMapping("/getRepo")
//    public ResponseEntity<UserServiceImpl> getUserRepo() {
//        return ResponseEntity.ok().body(userService);
//    }
//
//    @GetMapping("/getChat")
//    public ResponseEntity<Chat> getChatById(@RequestParam Long chatID) {
//        return ResponseEntity.ok().body(chatRepo.findById(chatID).orElse(null));
//    }
//
//    @GetMapping("/getUserForChatById")
//    public ResponseEntity<User> getUserById(@RequestParam Long userID) {
//        return ResponseEntity.ok().body(userRepo.findById(userID).orElse(null));
//    }
//
//    @GetMapping("/chatpage")
//    public String getChat(){
//        return "websocket";
//    }
//
//    @GetMapping("/lol")
//    public String getget(){
//        return "websocket";
//    }
//
//    @MessageMapping("/chat.sendMessage/{chatId}")
//    @SendTo("/topic/{chatId}")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable Long chatId, @Header(name = "userId") Long userId) {
//        Chat chat = chatRepo.findById(chatId).orElse(null);
//        Message message = new Message(chatMessage.getContent(), userId);
//        chat.getMessages().add(message);
//        messageRepo.save(message);
//        chatRepo.save(chat);
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser/{chatId}")
//    @SendTo("/topic/{chatId}")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor,
//                               @DestinationVariable Long chatId) {
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat/{chatId}")
//    @SendTo("/topic/{chatId}")
//    public ChatMessage message(@DestinationVariable Long chatId, @Payload ChatMessage chatMessage){
//        return chatMessage;
//    }
//
//}
