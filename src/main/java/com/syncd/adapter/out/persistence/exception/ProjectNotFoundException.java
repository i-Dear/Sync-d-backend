package com.syncd.adapter.out.persistence.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(String projectId) {
        super("Project with ID " + projectId + " does not exist.");
    }
}
