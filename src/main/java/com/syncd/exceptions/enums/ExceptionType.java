package com.syncd.exceptions.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
    REGISTER_ERROR(HttpStatus.BAD_REQUEST, "회원가입 에러"),
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "로그인 에러"),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트 못찾음"),
    PROJECT_OPERATION_ERROR(HttpStatus.BAD_REQUEST, "프로젝트 드리븐 에러"),

    PROJECT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "프로젝트 이미 존재");

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
