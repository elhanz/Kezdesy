package app.kezdesy.controller;


import app.kezdesy.entity.User;
import app.kezdesy.entity.VerificationToken;
import app.kezdesy.model.EmailRequest;
import app.kezdesy.registerVerification.event.RegistrationCompleteEvent;
import app.kezdesy.registerVerification.event.listener.RegistrationCompleteEventListener;
import app.kezdesy.model.NewPasswordRequest;
import app.kezdesy.registerVerification.passwordReset.RegisterService;
import app.kezdesy.repository.TokenRepository;
import app.kezdesy.service.implementation.UserServiceImpl;
import app.kezdesy.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final UserServiceImpl userService;
    private final ApplicationEventPublisher publisher;
    private final TokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;

    private final UserValidation userValidation ;

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user, final HttpServletRequest request) {

        if (!userValidation.isEmailValid(user.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email.");
        }
        if (!userValidation.isNameValid(user.getFirst_name())) {
            return ResponseEntity.badRequest().body("Invalid name.");
        }
        if (!userValidation.isNameValid(user.getLast_name())) {
            return ResponseEntity.badRequest().body("Invalid surname.");
        }
        if (!userValidation.isAgeValid(user.getAge())) {
            return ResponseEntity.badRequest().body("Invalid age.");
        }
        if (!userValidation.isGenderValid(user.getGender())) {
            return ResponseEntity.badRequest().body("Invalid gender.");
        }
        if (!userValidation.isPasswordValid(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password must contain 8 or more symbols.");
        }
        User newUser = userService.createUser(user);
        if (newUser != null) {
            publisher.publishEvent(new RegistrationCompleteEvent(newUser, applicationUrl(request)));
            return new ResponseEntity("Success!  Please, check your email for to complete your registration", HttpStatus.CREATED);

        }

       return ResponseEntity.badRequest().body("Email is already occupied.");
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping("/verifyEmail")
    public ModelAndView verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()) {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("message", "This account has already been verified, please, login.");
            return mav;
        }
        String verificationResult = registerService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            ModelAndView mav = new ModelAndView("success");
            mav.addObject("message", "Email verified successfully. Now you can login to your account.");
            return mav;
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Invalid verification token.");
        return mav;
    }


    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody EmailRequest emailRequest,
                                       final HttpServletRequest servletRequest)
            throws MessagingException, UnsupportedEncodingException {

        User user = userService.getUserByEmail(emailRequest.getEmail());

        if (user == null) {
            return "This email doesn't registered";
        }
        String passwordResetUrl = "";

        String passwordResetToken = UUID.randomUUID().toString();
        registerService.createPasswordResetTokenForUser(user, passwordResetToken);
        passwordResetUrl = passwordResetEmailLink(emailRequest.getEmail(),applicationUrl(servletRequest), passwordResetToken);

        return passwordResetUrl;
    }


    @GetMapping("/reset-password")
    public ModelAndView showResetPasswordForm(@RequestParam("token") String token, Model model) {
        String tokenVerificationResult = registerService.validateToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            model.addAttribute("error", "Invalid password reset token");
            return new ModelAndView("error", model.asMap());
        }
        Optional<User> theUser = registerService.findUserByPasswordToken(token);
        if (theUser.isPresent()) {
            model.addAttribute("token", token);
            return new ModelAndView("pasresdone", model.asMap());
        }
        model.addAttribute("error", "Invalid password reset token");
        return new ModelAndView("error", model.asMap());
    }

    @PostMapping("/reset-password")
    public ModelAndView resetPassword(@ModelAttribute("newPasswordRequest") NewPasswordRequest newPasswordRequest,
                                      @RequestParam("token") String token, Model model) {
        String tokenVerificationResult = registerService.validateToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            model.addAttribute("error", "Invalid password reset token");
            return new ModelAndView("error", model.asMap());
        }
        Optional<User> theUser = registerService.findUserByPasswordToken(token);
        if (theUser.isPresent()) {
            registerService.resetPassword(theUser.get(), newPasswordRequest.getNewPassword());
            return new ModelAndView("success");
        }
        model.addAttribute("error", "Invalid password reset token");
        return new ModelAndView("error", model.asMap());
    }

    private String passwordResetEmailLink(String email, String applicationUrl,
                                          String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/reset-password?token=" + passwordToken;
        eventListener.sendPasswordResetVerificationEmail(email, url);
        log.info("Click the link to reset your password :  {}", url);
        return url;
    }


}
