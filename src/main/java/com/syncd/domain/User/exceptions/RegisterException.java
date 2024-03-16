package com.syncd.domain.User.exceptions;

public class RegisterException extends RuntimeException{
    public RegisterException(String message) {
        super(message);
    }
}
