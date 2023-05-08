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





    public boolean isUserValid(User user) {
        return isEmailValid(user.getEmail())
                && isAgeValid(user.getAge())
                && isNameValid(user.getFirst_name())
                && isNameValid(user.getLast_name())
                && isPasswordValid(user.getPassword())
                && isGenderValid(user.getGender());

//                && isCityValid(user.getCity());
    }

    private boolean isEmailValid(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isAgeValid(int age) {
        return age > 18 && age < 110;
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



    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }

    private boolean isGenderValid(String gender) {
        return gender != null && (gender.equals("Male") || gender.equals("Female") );
    }


//    public boolean isCityValid(String city){
//        ArrayList<String> cities = new ArrayList<>();
//
//        for(String str: cities){
//            if(city.equals(str)){
//                return true;
//            }
//        }
//        return false;
//    }
}
