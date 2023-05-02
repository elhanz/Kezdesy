package app.kezdesy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private ChatRepo chatRepo;

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
