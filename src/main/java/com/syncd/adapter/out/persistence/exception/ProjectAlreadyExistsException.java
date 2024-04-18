package com.syncd.adapter.out.persistence.exception;

public class ProjectAlreadyExistsException extends RuntimeException {
    public ProjectAlreadyExistsException(String projectId) {
        super("Project with ID " + projectId + " already exists.");
    }
}