package com.syncd.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorInfo {
    REGISTER_ERROR(HttpStatus.BAD_REQUEST, "Registration error", 400001),
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "Login error", 401001),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found", 404001),
    PROJECT_OPERATION_ERROR(HttpStatus.BAD_REQUEST, "Project operation error", 400002),
    PROJECT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Project already exists", 409001),
    NOT_LEFT_CHANCE(HttpStatus.FORBIDDEN, "Not Left Chance", 403001),
    NOT_INCLUDE_PROJECT(HttpStatus.FORBIDDEN, "Not include that project", 403002),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found", 404002);

    private final HttpStatus status;
    private final String message;
    private final int detailStatusCode;

    ErrorInfo(HttpStatus status, String message, int detailStatusCode) {
        this.status = status;
        this.message = message;
        this.detailStatusCode = detailStatusCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getDetailStatusCode() {
        return detailStatusCode;
    }
}
