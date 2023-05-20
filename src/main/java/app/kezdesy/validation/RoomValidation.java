package app.kezdesy.validation;


public class RoomValidation {


    public boolean isHeaderCorrect(int length) {
        return length >= 3 || length <= 200;
    }

    public boolean isDescriptionCorrect(int length) {
        return length <= 600;
    }

    public boolean isMaxMembersCorrect(int members) {
        return members > 1 || members < 21;
    }


    public boolean isAgeLimitCorrect(int minAge, int maxAge) {
        return maxAge >= minAge || maxAge <= 110 || maxAge >= 18 || minAge <= 110 || minAge >= 18;
    }


}
