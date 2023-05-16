package app.kezdesy.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {

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

    @GetMapping("/setInterests")
    public String setInterests() {
        return "interests";
    }

    @GetMapping("/chats/chatpage")
    public String getChat() {
        return "websocket";
    }

    @GetMapping("/rooms")
    public String getRoomsPage() {
        return "rooms";
    }

    @GetMapping("/editRoom")
    public String getEditRoomPage() {
        return "editRoom";
    }

    @GetMapping("/termsPolicy")
    public String getTermsPolicy() {
        return "termsPolicy";
    }

    @GetMapping("/password-reset/request")
    public String getPasswordResetRequest() {
        return "pasresreq";
    }
}
