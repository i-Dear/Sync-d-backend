package com.syncd.exceptions.enums;

public enum LogMessageType {
    REGISTER_ERROR("registration 중 에러 발생"),
    LOGIN_ERROR("login 중 에러 발생"),
    PROJECT_OPERATION_ERROR("project operation 중 에러 발생"),
    PROJECT_NOT_FOUND("Project 못 찾음");

    private final String message;

    LogMessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}