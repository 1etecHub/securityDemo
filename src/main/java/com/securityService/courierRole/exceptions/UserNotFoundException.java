package com.securityService.courierRole.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
