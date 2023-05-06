package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;

import java.util.List;

public interface IUserService {

    boolean saveUser (User user);

    User getUserByEmail(String email);
    User findById(Long id);

}
