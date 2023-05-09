package app.kezdesy.validation;

import app.kezdesy.entity.User;
import app.kezdesy.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserValidation {

    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public boolean isEmailValid(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean isAgeValid(int age) {
        return age >= 18 && age < 110;
    }


    public static boolean isNameValid(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        String regex = "^[\\p{L}]{2,50}$";
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }

    public boolean isGenderValid(String gender) {
        return gender != null && (gender.equals("Male") || gender.equals("Female") );
    }

}
