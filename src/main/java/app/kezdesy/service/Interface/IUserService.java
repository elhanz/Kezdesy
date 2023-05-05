package app.kezdesy.service.Interface;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;

import java.util.List;

public interface IUserService {

    User saveUser (User user);
    Role saveRole (Role role);
    void addRoleToUser (String email, String roleName);
    User getUser (String email);
    List<User> getAllUsers();
}
