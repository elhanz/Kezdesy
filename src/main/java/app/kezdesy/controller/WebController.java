package app.kezdesy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WebController {

    @GetMapping("/termsPolicy")
    public String getTermsPolicy(@RequestParam("lang") String language) {

        if (language.equals("kz")) {
            return "termsPolicy_kz";
        } else if (language.equals("ru")) {
            return "termsPolicy_ru";
        } else {
            return "termsPolicy";
        }

    }


    @GetMapping("/auth")
    public String getPage(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "register_kz";
        } else if (language.equals("ru")) {
            return "register_ru";
        } else {
            return "register";
        }
    }

    @GetMapping("/")
    public String getHomePage(@RequestParam("lang") String language) {

        if (language.equals("kz")) {
            return "index_kz";
        } else if (language.equals("ru")) {
            return "index_ru";
        } else {
            return "index";
        }

    }

    @GetMapping("/loginUser")
    public String getLoginPage(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "login_kz";
        } else if (language.equals("ru")) {
            return "login_ru";
        } else {
            return "login";
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "profile_kz";
        } else if (language.equals("ru")) {
            return "profile_ru";
        } else {
            return "profile";
        }
    }

    @GetMapping("/chats")
    public String getChats(@RequestParam("lang") String language) {

        if (language.equals("kz")) {
            return "chat_kz";
        } else if (language.equals("ru")) {
            return "chat_ru";
        } else {
            return "chat";
        }
    }

    @GetMapping("/updateUser")
    public String getEditPage(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "updateUser_kz";
        } else if (language.equals("ru")) {
            return "updateUser_ru";
        } else {
            return "updateUser";
        }

    }

    @GetMapping("/createRoom")
    public String getCreateRoomPage(@RequestParam("lang") String language) {

        if (language.equals("kz")) {
            return "addRoom_kz";
        } else if (language.equals("ru")) {
            return "addRoom_ru";
        } else {
            return "addRoom";
        }

    }

    @GetMapping("/setInterests")
    public String setInterests(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "interests_kz";
        } else if (language.equals("ru")) {
            return "interests_ru";
        } else {
            return "interests";
        }

    }

    @GetMapping("/rooms")
    public String getRoomsPage(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "rooms_kz";
        } else if (language.equals("ru")) {
            return "rooms_ru";
        } else {
            return "rooms";
        }
    }

    @GetMapping("/editRoom")
    public String getEditRoomPage(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "editRoom_kz";
        } else if (language.equals("ru")) {
            return "editRoom_ru";
        } else {
            return "editRoom";
        }
    }


    @GetMapping("/password-reset/request")
    public String getPasswordResetRequest(@RequestParam("lang") String language) {
        if (language.equals("kz")) {
            return "pasresreq_kz";
        } else if (language.equals("ru")) {
            return "pasresreq_ru";
        } else {
            return "pasresreq";
        }
    }
}
