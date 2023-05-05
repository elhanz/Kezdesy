package kezdesy.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kezdesy.entities.Role;
import kezdesy.entities.User;
import kezdesy.repositories.RoleRepo;
import kezdesy.repositories.UserRepo;
import kezdesy.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class MainController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private ArrayList<String> cities = new ArrayList<>();

    public int userIsValid(User user){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user.getEmail());
        if(!userRepo.existsByEmail(user.getEmail())){
            if(user.getAge() < 120 && user.getAge() > 5){
                if(matcher.matches()){
                    if(validateString(user.getFirst_name()) && validateString(user.getLast_name())){
                        if(validCity(user.getCity())){
                            if(user.getGender().equals("Male") || user.getGender().equals("Female") || user.getGender().equals("Other")){
                                return 0;
                            }else{
                                return 7;
                            }
                        }else{
                            return 6;
                        }
                    }else{
                        return 4;
                    }
                }else{
                    return 3;
                }
            }else{
                return 2;
            }
        }else{
            return 1;
        }
    }

    @PostMapping("/registerGoogle")
    public ResponseEntity registerGoogle(@RequestBody User user){
        if(!userRepo.existsByEmail(user.getEmail())) {
            user.getRoles().add(roleRepo.findByName("ROLE_USER"));
            user.setPassword(passwordEncoder.encode("12345678"));
            userRepo.save(user);
            return new ResponseEntity("User was added", HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity("User is exist", HttpStatus.CREATED);
        }
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user.getEmail());
        if(!userRepo.existsByEmail(user.getEmail())){
            if(user.getAge() < 120 && user.getAge() > 5){
                if(matcher.matches()){
                    if(validateString(user.getFirst_name()) && validateString(user.getLast_name())){
                        if(user.getPassword().length() >= 8){
                            if(user.getGender().equals("Male") || user.getGender().equals("Female") || user.getGender().equals("Other")){
                                user.getRoles().add(roleRepo.findByName("ROLE_USER"));
                                user.setPassword(passwordEncoder.encode(user.getPassword()));
                                userRepo.save(user);
                                return new ResponseEntity("User was added", HttpStatus.CREATED);
                            }else{
                                return ResponseEntity.badRequest().body("Incorrect gender.\nChoose between Male, Female or Other.");
                            }
                        }else{
                            return ResponseEntity.badRequest().body("Password must contain at least 8 characters.");
                        }
                    }else{
                        return ResponseEntity.badRequest().body("Incorrect name or lastname.");
                    }
                }else{
                    return ResponseEntity.badRequest().body("Incorrect email address.");
                }
            }else{
                return ResponseEntity.badRequest().body("Incorrect age.");
            }
        }else{
            return ResponseEntity.badRequest().body("User already exist.");
        }
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("zxcxz".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                User user = userRepo.findByEmail(email);
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
            } catch (Exception e){
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else {
            throw new RuntimeException("Refresh token is missing.");
        }
    }

    public boolean validateString(String str) {
        str = str.toLowerCase();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (!(ch >= 'a' && ch <= 'z')) {
                return false;
            }
        }
        return true;
    }

    public boolean validCity(String city){
        for(String str: cities){
            if(city.equals(str)){
                return true;
            }
        }
        return false;
    }
}
