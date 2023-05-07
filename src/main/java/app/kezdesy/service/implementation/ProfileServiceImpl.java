package app.kezdesy.service.implementation;

import app.kezdesy.entity.User;
import app.kezdesy.repository.UserRepo;
import app.kezdesy.service.interfaces.IProfileService;
import app.kezdesy.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {
    @Autowired
    UserRepo userRepo;
    public final PasswordEncoder passwordEncoder;

    private final UserValidation userValidation = new UserValidation(userRepo);

    public boolean changePhoto(String email, String file) {
        String picture = file.replace("{\"file\":\"", "");
        picture = picture.replace("\"}", "");
        User user = userRepo.findByEmail(email);
        user.setProfilePic(picture);

        userRepo.save(user);
        return true;
    }

    public boolean updateUser(User user) {
//        if (userValidation.isUserValid(user)) {
            User existUser = userRepo.findByEmail(user.getEmail());
            existUser.setFirst_name(user.getFirst_name());
            existUser.setLast_name(user.getLast_name());
            existUser.setAge(user.getAge());
            existUser.setGender(user.getGender());
            existUser.setCity(user.getCity());

            userRepo.save(existUser);
            return true;
//        }
//
//        return false;
    }

    @Override
    public boolean updateUserPassword(String email, String password) { //TODO Нужно передавать пароль зашифрованный
        User existUser = userRepo.findByEmail(email);
        if (existUser == null) return false;
        existUser.setPassword(passwordEncoder.encode(password));

        userRepo.save(existUser);
            return true;
        }


    public boolean deleteUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) return false;

        userRepo.delete(user);
        return true;
    }

}
