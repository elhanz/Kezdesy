package app.kezdesy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class WebController {

    @GetMapping("/termsPolicy")
    public String getTermsPolicy(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);

        if (language.equals("kz")) {
            return "termsPolicy_kz";
        } else if (language.equals("ru")) {
            return "termsPolicy_ru";
        } else {
            return "termsPolicy";
        }

    }


    @GetMapping("/auth")
    public String getPage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "register_kz";
        } else if (language.equals("ru")) {
            return "register_ru";
        } else {
            return "register";
        }
    }

    @GetMapping("/")
    public String getHomePage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "index_kz";
        } else if (language.equals("ru")) {
            return "index_ru";
        } else {
            return "index";
        }
    }

    @GetMapping("/loginUser")
    public String getLoginPage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "login_kz";
        } else if (language.equals("ru")) {
            return "login_ru";
        } else {
            return "login";
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "profile_kz";
        } else if (language.equals("ru")) {
            return "profile_ru";
        } else {
            return "profile";
        }
    }

    @GetMapping("/chats")
    public String getChats(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);

        if (language.equals("kz")) {
            return "chat_kz";
        } else if (language.equals("ru")) {
            return "chat_ru";
        } else {
            return "chat";
        }
    }

    @GetMapping("/updateUser")
    public String getEditPage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "updateUser_kz";
        } else if (language.equals("ru")) {
            return "updateUser_ru";
        } else {
            return "updateUser";
        }

    }

    @GetMapping("/createRoom")
    public String getCreateRoomPage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);

        if (language.equals("kz")) {
            return "addRoom_kz";
        } else if (language.equals("ru")) {
            return "addRoom_ru";
        } else {
            return "addRoom";
        }

    }

    @GetMapping("/setInterests")
    public String setInterests(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "interests_kz";
        } else if (language.equals("ru")) {
            return "interests_ru";
        } else {
            return "interests";
        }

    }

    @GetMapping("/rooms")
    public String getRoomsPage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "rooms_kz";
        } else if (language.equals("ru")) {
            return "rooms_ru";
        } else {
            return "rooms";
        }
    }

    @GetMapping("/editRoom")
    public String getEditRoomPage(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "editRoom_kz";
        } else if (language.equals("ru")) {
            return "editRoom_ru";
        } else {
            return "editRoom";
        }
    }


    @GetMapping("/password-reset/request")
    public String getPasswordResetRequest(HttpServletRequest request) {
        String language = getLanguageFromCookie(request);
        if (language.equals("kz")) {
            return "pasresreq_kz";
        } else if (language.equals("ru")) {
            return "pasresreq_ru";
        } else {
            return "pasresreq";
        }
    }

    private String getLanguageFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lang")) {
                    return cookie.getValue();
                }
            }
        }
        return "en"; // Default to English if the language cookie is not found
    }

    @PostMapping("/language")
    public String setLanguageCookie(@RequestParam("language") String language, HttpServletResponse response) {
        Cookie languageCookie = new Cookie("lang", language);
        languageCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days expiration
        response.addCookie(languageCookie);
        return "redirect:/";
    }
}
