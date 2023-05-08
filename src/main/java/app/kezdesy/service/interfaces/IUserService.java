package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;

import java.util.List;

public interface IUserService {

    boolean createUser (User user);

    User getUserByEmail(String email);
    User findById(Long id);

}
