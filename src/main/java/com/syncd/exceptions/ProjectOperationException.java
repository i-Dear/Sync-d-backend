package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class ProjectOperationException extends RuntimeException {
    public ProjectOperationException() {
        super(ExceptionType.PROJECT_OPERATION_ERROR.getMessage());
    }
}
