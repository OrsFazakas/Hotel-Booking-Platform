package edu.ntt.hotelbookingplatform.exception.advice;

import edu.ntt.hotelbookingplatform.exception.UserWrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserAlreadyExistsAdvice {
    @ExceptionHandler(UserWrongPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String employeeNotFoundHandler(UserWrongPasswordException userWrongPasswordException) {
        return userWrongPasswordException.getMessage();
    }
}