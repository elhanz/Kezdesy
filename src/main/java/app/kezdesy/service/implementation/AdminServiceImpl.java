package app.kezdesy.service.implementation;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;
import app.kezdesy.repository.RoleRepo;
import app.kezdesy.repository.UserRepo;
import app.kezdesy.service.interfaces.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final RoleRepo roleRepo;

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public boolean addRoleToUser(String email, String roleName) {
        User user = userRepo.findByEmail(email);
        if (user == null || roleRepo.findByName(roleName) == null ) return false;
        user.getRoles().add(roleRepo.findByName(roleName));
        userRepo.save(user);
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
