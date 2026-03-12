package edu.ntt.hotelbookingplatform.exception.user;

public class CannotChooseOwnUserException extends RuntimeException {
    public CannotChooseOwnUserException() {
        super("Cannot use this method on your own account!");
    }
}
