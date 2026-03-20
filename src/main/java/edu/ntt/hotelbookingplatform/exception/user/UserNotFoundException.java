package edu.ntt.hotelbookingplatform.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Users with email " + email + " could not be found!");
    }
}
