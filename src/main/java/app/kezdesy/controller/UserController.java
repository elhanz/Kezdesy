package app.kezdesy.controller;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;
import app.kezdesy.repository.RoleRepository;
import app.kezdesy.repository.UserRepository;
import app.kezdesy.service.Implementation.UserServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private ArrayList<String> cities = new ArrayList<>();


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        if (userService.register(user)) {
            return new ResponseEntity("User was registered", HttpStatus.CREATED);
        }

        return new ResponseEntity("User wasn't registered", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @PostMapping("/loginUser")
//    public ResponseEntity loginUser(@RequestBody User user) {
//        if (userService.loginUser(user)) {
//            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//            String access_token = JWT.create()
//                    .withSubject(user.getEmail())
//                    .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
//                    .withClaim("roles", userService.getAuthority(userService.getByEmail(user.getEmail())).stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                    .sign(algorithm);
//
//            return ResponseEntity.ok().body(access_token);
//        } else
//            return ResponseEntity.badRequest().body("incorrect email or password");
//    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("zxcxz".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                User user = userRepository.findByEmail(email);
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
