package edu.ntt.hotelbookingplatform.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Users with email " + email + " already exists! Try logging in instead!");
    }
}
