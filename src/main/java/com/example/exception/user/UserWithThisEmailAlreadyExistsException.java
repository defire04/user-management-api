package com.example.exception.user;

import com.example.exception.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class UserWithThisEmailAlreadyExistsException extends ClientException {

    private static final String DEFAULT_MESSAGE = "User with this email already exists";

    public UserWithThisEmailAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public UserWithThisEmailAlreadyExistsException(String email) {
        super(String.format("User with email %s already exists", email));
    }
}