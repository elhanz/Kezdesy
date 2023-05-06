package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;

import java.util.List;

public interface IUserService {

    boolean saveUser (User user);
    Role saveRole (Role role);
    boolean addRoleToUser (String email, String roleName);
    User getUserByEmail(String email);
    User findById(Long id);
    List<User> getAllUsers();
}
