package edu.ntt.hotelbookingplatform.exception.user;

public class UserWrongPasswordException extends RuntimeException {
    public UserWrongPasswordException() {
        super("Password is incorrect!");
    }
}
