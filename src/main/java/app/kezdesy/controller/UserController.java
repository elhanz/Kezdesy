package app.kezdesy.controller;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;
import app.kezdesy.entity.VerificationToken;
import app.kezdesy.event.RegistrationCompleteEvent;
import app.kezdesy.repository.VerificationTokenRepository;
import app.kezdesy.service.implementation.UserServiceImpl;
import app.kezdesy.validation.UserValidation;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserServiceImpl userService;

    private final UserValidation userValidation = new UserValidation();

    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;


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


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("Sho5o4o1ic".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                User user = userService.getUserByEmail(email);
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing.");
        }
    }

}
