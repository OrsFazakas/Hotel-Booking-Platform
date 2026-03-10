package edu.ntt.hotelbookingplatform.exception;

public class UserWrongPasswordException extends RuntimeException {
    public UserWrongPasswordException() {
        super("Old password is incorrect!");
    }
}
