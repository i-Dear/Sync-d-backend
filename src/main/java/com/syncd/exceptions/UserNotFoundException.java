package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String identifier) {
        super(identifier + ": " + ExceptionType.USER_NOT_FOUND.getMessage());
    }
}
