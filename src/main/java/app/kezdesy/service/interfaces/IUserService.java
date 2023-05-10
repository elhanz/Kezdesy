package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;

import java.util.List;

public interface IUserService {

    User createUser (User user);

    User getUserByEmail(String email);
    User findById(Long id);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);
}
