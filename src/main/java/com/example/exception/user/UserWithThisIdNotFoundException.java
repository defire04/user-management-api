package com.example.exception.user;


import com.example.exception.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserWithThisIdNotFoundException extends ClientException {

    private static final String DEFAULT_MESSAGE = "User with this id not found";

    public UserWithThisIdNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserWithThisIdNotFoundException(Long userId) {
        super(String.format("User with id %d not found", userId));
    }
}