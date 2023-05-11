package app.kezdesy.registerVerification;


import app.kezdesy.entity.User;
import app.kezdesy.entity.VerificationToken;
import app.kezdesy.registerVerification.event.RegistrationCompleteEvent;
import app.kezdesy.registerVerification.event.listener.RegistrationCompleteEventListener;
import app.kezdesy.registerVerification.passwordReset.PasswordResetRequest;
import app.kezdesy.repository.VerificationTokenRepository;
import app.kezdesy.service.implementation.UserServiceImpl;
import app.kezdesy.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserServiceImpl userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final HttpServletRequest servletRequest;
    private final UserValidation userValidation = new UserValidation();


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

        return new ResponseEntity("Email is already occupied ", HttpStatus.BAD_REQUEST);
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()) {
            return new ResponseEntity("\"This account has already been verified, please, login.\"", HttpStatus.BAD_REQUEST);

        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return new ResponseEntity("Email verified successfully. Now you can login to your account", HttpStatus.OK);

        }
        return ResponseEntity.badRequest().body("Invalid verification token");
    }


    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        resendRegistrationVerificationTokenEmail(applicationUrl(request), verificationToken);
        return "A new verification link has been sent to your email," +
                " please, check to activate your account";
    }

    private void resendRegistrationVerificationTokenEmail( String applicationUrl, VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/register/verifyEmail?token=" + verificationToken.getToken();
        eventListener.sendVerificationEmail(url);
        log.info("Click the link to verify your registration :  {}", url);
    }

    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest,
                                       final HttpServletRequest servletRequest)
            throws MessagingException, UnsupportedEncodingException {

        User user = userService.getUserByEmail(passwordResetRequest.getEmail());

        if (user == null) {
            return "This email doesn't registered";
        }
        String passwordResetUrl = "";

            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(applicationUrl(servletRequest), passwordResetToken);

        return passwordResetUrl;
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String token) {
        String tokenVerificationResult = userService.validatePasswordResetToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "Invalid token password reset token";
        }
        Optional<User> theUser = Optional.ofNullable(userService.findUserByPasswordToken(token));
        if (theUser.isPresent()) {
            userService.resetPassword(theUser.get(), passwordResetRequest.getNewPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }
    private String passwordResetEmailLink( String applicationUrl,
                                          String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/reset-password?token=" + passwordToken;
        eventListener.sendPasswordResetVerificationEmail(url);
        log.info("Click the link to reset your password :  {}", url);
        return url;
    }



}
