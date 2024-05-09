package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String identifier) {
        super("User with " + identifier + " not found: " + ExceptionType.USER_NOT_FOUND.getMessage());
    }
}
