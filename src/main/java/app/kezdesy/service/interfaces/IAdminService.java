package app.kezdesy.service.interfaces;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;

import java.util.List;

public interface IAdminService {
    boolean addRoleToUser(String email, String roleName);

    Role saveRole(Role role);

    List<User> getAllUsers();

}
