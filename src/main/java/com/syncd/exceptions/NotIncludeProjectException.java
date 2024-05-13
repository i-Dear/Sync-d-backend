package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class NotIncludeProjectException extends RuntimeException {
    public NotIncludeProjectException() {
        super(ExceptionType.NOT_LEFT_CHANCE.getMessage());
    }

    public NotIncludeProjectException(String projectId){
        super("Project ID " + projectId + " : " + ExceptionType.NOT_LEFT_CHANCE.getMessage());
    }
}
