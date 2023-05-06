package app.kezdesy.service.interfaces;

import app.kezdesy.entity.User;

public interface IProfileService {

    boolean changePhoto (String email, String file);
    boolean updateUser (User user);

    boolean updateUserPassword (String email, String password);
    boolean deleteUserByEmail (String email);

}
