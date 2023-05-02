package app.kezdesy.service;


import app.kezdesy.entity.Role;
import app.kezdesy.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    public void addRole(String newRole) {
        roleRepository.save(new Role(newRole));
    }

}
