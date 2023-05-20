package app.kezdesy.service.implementation;

import app.kezdesy.entity.Role;
import app.kezdesy.entity.User;
import app.kezdesy.repository.RoleRepository;
import app.kezdesy.repository.UserRepository;
import app.kezdesy.service.interfaces.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepo;

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public boolean addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        if (user == null || roleRepo.findByName(roleName) == null) return false;
        user.getRoles().add(roleRepo.findByName(roleName));
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
