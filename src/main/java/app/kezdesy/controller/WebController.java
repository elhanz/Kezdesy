package app.kezdesy.controller;

import app.kezdesy.repository.ChatRepo;
import app.kezdesy.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class WebController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ChatRepo chatRepo;

    @GetMapping("/auth")
    public String getPage() {
        return "register";
    }

    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/loginUser")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/profile")
    public String getProfilePage() {
        return "profile";
    }

    @GetMapping("/chats")
    public String getChats() {
        return "chat";
    }

    @GetMapping("/updateUser")
    public String getEditPage() {
        return "updateUser";
    }

    @GetMapping("/createRoom")
    public String getCreateRoomPage() {
        return "addroom";
    }
}