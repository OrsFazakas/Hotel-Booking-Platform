package edu.ntt.hotelbookingplatform.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User with email " + email + " could not be found!");
    }
}
