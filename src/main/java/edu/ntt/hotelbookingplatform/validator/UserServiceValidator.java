package edu.ntt.hotelbookingplatform.validator;

public class UserServiceValidator {
    public Boolean validatePassword(String actualPassword, String typedPassword){
        return actualPassword.equals(typedPassword);
    }
}
