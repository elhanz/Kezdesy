package app.kezdesy.service.implementation;

import app.kezdesy.entity.Interest;
import app.kezdesy.entity.User;
import app.kezdesy.repository.UserRepo;
import app.kezdesy.service.interfaces.IProfileService;
import app.kezdesy.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {
    @Autowired
    UserRepo userRepo;
    public final PasswordEncoder passwordEncoder;

    private UserValidation userValidation = new UserValidation();

    public boolean changePhoto(String email, String file) {
        String picture = file.replace("{\"file\":\"", "");
        picture = picture.replace("\"}", "");
        User user = userRepo.findByEmail(email);
        user.setProfilePic(picture);

        userRepo.save(user);
        return true;
    }

    public boolean updateUser(User user) {
        User existUser = userRepo.findByEmail(user.getEmail());
//        if (userValidation.isUserValid(user) && existUser != null) {

            existUser.setFirst_name(user.getFirst_name());
            existUser.setLast_name(user.getLast_name());
            existUser.setAge(user.getAge());
            existUser.setGender(user.getGender());
            existUser.setCity(user.getCity());

            userRepo.save(existUser);
            return true;
        }

//        return false;
//    }

    @Override
    public boolean setInterests(String email, Set<Interest> interests) {

        User existUser = userRepo.findByEmail(email);
        existUser.setInterests(interests);
        userRepo.save(existUser);

        return true;
    }

    @Override
    public boolean updateUserPassword(String email, String oldPassword, String newPassword) { //TODO Нужно передавать пароль зашифрованный

        User existUser = userRepo.findByEmail(email);

        if (existUser != null && passwordEncoder.matches(oldPassword, existUser.getPassword())) {
            existUser.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(existUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        userRepo.deleteById(user.getId());
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) return null;
        return user;
    }
}
