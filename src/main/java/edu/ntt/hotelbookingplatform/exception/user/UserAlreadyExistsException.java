package edu.ntt.hotelbookingplatform.exception.user;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Users with email " + email + " already exists! Try logging in instead!");
    }
}
