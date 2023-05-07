package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Interest;
import app.kezdesy.entity.User;
import app.kezdesy.model.ChangeInterestsRequest;

import java.util.Set;

public interface IProfileService {

    boolean changePhoto (String email, String file);
    boolean updateUser (User user);

    User getUserByEmail(String email);

    boolean setInterests(String email, Set<Interest> interests);

    boolean updateUserPassword (String email, String password);
    boolean deleteUserByEmail (String email);

}
