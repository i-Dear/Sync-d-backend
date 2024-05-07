package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class RegisterException extends RuntimeException {
    public RegisterException() {
        super(ExceptionType.REGISTER_ERROR.getMessage());
    }
}

