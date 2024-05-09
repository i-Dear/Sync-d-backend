package com.syncd.exceptions;

import com.syncd.exceptions.enums.ExceptionType;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
        super(ExceptionType.PROJECT_NOT_FOUND.getMessage());
    }
    public ProjectNotFoundException(String projectId) {
        super("Project with ID " + projectId + " not found: " + ExceptionType.PROJECT_NOT_FOUND.getMessage());
    }
}

