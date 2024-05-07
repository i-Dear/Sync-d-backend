package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class LoginException extends RuntimeException {
    public LoginException() {
        super(ExceptionType.LOGIN_ERROR.getMessage());
    }

}