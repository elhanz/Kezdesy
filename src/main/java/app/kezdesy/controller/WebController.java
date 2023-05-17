package app.kezdesy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WebController {


//    @GetMapping("/")
//    public String getHomePage(@RequestParam("lang") String language) {
//
//        if (language.equals("kz")) {
//            return "redirect:/index_kz.html";
//        } else if (language.equals("en")) {
//            return "redirect:/index_en.html";
//        } else {
//            return "redirect:/index_ru.html";
//        }
//
//    }

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
        return "chat_kz";
    }

    @GetMapping("/updateUser")
    public String getEditPage() {
        return "updateUser";
    }

    @GetMapping("/createRoom")
    public String getCreateRoomPage() {
        return "addroom_en";
    }

    @GetMapping("/setInterests")
    public String setInterests() {
        return "interests";
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
