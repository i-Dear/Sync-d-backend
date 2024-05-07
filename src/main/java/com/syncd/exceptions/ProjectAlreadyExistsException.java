package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class ProjectAlreadyExistsException extends RuntimeException {
    public ProjectAlreadyExistsException() {
        super(ExceptionType.PROJECT_ALREADY_EXISTS.getMessage());
    }
}
