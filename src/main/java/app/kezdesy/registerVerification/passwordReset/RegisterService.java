package app.kezdesy.registerVerification.passwordReset;

import app.kezdesy.entity.User;
import app.kezdesy.entity.VerificationToken;
import app.kezdesy.repository.UserRepository;
import app.kezdesy.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;

    public final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository tokenRepository;




    public Optional<User> findUserByPasswordToken(String passwordResetToken) {
        return Optional.ofNullable(tokenRepository.findByToken(passwordResetToken).getUser());
    }

    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        VerificationToken token = tokenRepository.findByUserId(user.getId());
        token.setToken(passwordToken);
        tokenRepository.save(token);
    }


    public void resetPassword(User theUser, String newPassword) {
        theUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(theUser);
    }


}
