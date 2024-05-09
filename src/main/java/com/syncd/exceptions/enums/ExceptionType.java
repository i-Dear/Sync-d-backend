package com.syncd.exceptions.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
    REGISTER_ERROR(HttpStatus.BAD_REQUEST, "Registration error"),
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "Login error"),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found"),
    PROJECT_OPERATION_ERROR(HttpStatus.BAD_REQUEST, "Project operation error"),
    PROJECT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Project already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found");
    private final HttpStatus status;
    private final String message;

    ExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
