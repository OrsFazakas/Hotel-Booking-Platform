package edu.ntt.hotelbookingplatform.exception.advice.user;

import edu.ntt.hotelbookingplatform.exception.user.CannotChooseOwnUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CannotChooseOwnUserAdvice {
    @ExceptionHandler(CannotChooseOwnUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String employeeNotFoundHandler(CannotChooseOwnUserException cannotChooseOwnUserException) {
        return cannotChooseOwnUserException.getMessage();
    }
}
