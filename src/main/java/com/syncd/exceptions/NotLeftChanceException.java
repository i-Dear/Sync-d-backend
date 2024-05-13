package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class NotLeftChanceException extends RuntimeException {
    public NotLeftChanceException() {
        super(ExceptionType.NOT_INCLUDE_PROJECT.getMessage());
    }

    public NotLeftChanceException(String projectId){
        super("Project ID " + projectId + " : " + ExceptionType.NOT_INCLUDE_PROJECT.getMessage());
    }
}
