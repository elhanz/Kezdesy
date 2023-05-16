package app.kezdesy.service.implementation;

import app.kezdesy.entity.Interest;
import app.kezdesy.entity.Room;
import app.kezdesy.entity.User;
import app.kezdesy.entity.VerificationToken;
import app.kezdesy.repository.RoomRepository;
import app.kezdesy.repository.TokenRepository;
import app.kezdesy.repository.UserRepository;
import app.kezdesy.service.interfaces.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    public final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    public boolean changePhoto(String email, String file) {
        String picture = file.replace("{\"file\":\"", "");
        picture = picture.replace("\"}", "");
        User user = userRepository.findByEmail(email);
        user.setProfilePic(picture);

        userRepository.save(user);
        return true;
    }

    public boolean updateUser(User user) {
        User existUser = userRepository.findByEmail(user.getEmail());
        existUser.setFirst_name(user.getFirst_name());
        existUser.setLast_name(user.getLast_name());
        existUser.setAge(user.getAge());
        existUser.setGender(user.getGender());
        existUser.setCity(user.getCity());

        userRepository.save(existUser);
        return true;
    }


    @Override
    public boolean setInterests(String email, Set<Interest> interests) {

        User existUser = userRepository.findByEmail(email);
        existUser.setInterests(interests);
        userRepository.save(existUser);

        return true;
    }

    @Override
    public boolean updateUserPassword(String email, String oldPassword, String newPassword) { //TODO Нужно передавать пароль зашифрованный

        User existUser = userRepository.findByEmail(email);

        if (existUser != null && passwordEncoder.matches(oldPassword, existUser.getPassword())) {
            existUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(existUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        VerificationToken token = tokenRepository.findByUserId(user.getId());

        if (token != null) {
            tokenRepository.delete(token);
        }
        List<Room> userRooms = roomRepository.myRooms(user.getId());

        userRooms.forEach(room -> {
            if (room.getOwner() == user.getEmail()) {
                roomRepository.delete(room);
            } else {

                room.getUsers().remove(user);

            }

        });

        userRepository.delete(user);
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        return user;
    }

    @Override
    public List<Room> getUserRooms(String email) {
        User user = userRepository.findByEmail(email);
        List<Room> rooms = roomRepository.myRooms(user.getId());
        if (rooms == null) {
            return null;
        }
        return rooms;
    }
}
